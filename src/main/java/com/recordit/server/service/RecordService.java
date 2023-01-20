package com.recordit.server.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.constant.RefType;
import com.recordit.server.domain.Comment;
import com.recordit.server.domain.ImageFile;
import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;
import com.recordit.server.domain.RecordCategory;
import com.recordit.server.domain.RecordColor;
import com.recordit.server.domain.RecordIcon;
import com.recordit.server.dto.record.MemoryRecordResponseDto;
import com.recordit.server.dto.record.RecordDetailResponseDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.dto.record.WriteRecordResponseDto;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.record.NotMatchLoginUserWithRecordWriterException;
import com.recordit.server.exception.record.RecordColorNotFoundException;
import com.recordit.server.exception.record.RecordIconNotFoundException;
import com.recordit.server.exception.record.RecordNotFoundException;
import com.recordit.server.exception.record.category.RecordCategoryNotFoundException;
import com.recordit.server.repository.CommentRepository;
import com.recordit.server.repository.ImageFileRepository;
import com.recordit.server.repository.MemberRepository;
import com.recordit.server.repository.RecordCategoryRepository;
import com.recordit.server.repository.RecordColorRepository;
import com.recordit.server.repository.RecordIconRepository;
import com.recordit.server.repository.RecordRepository;
import com.recordit.server.util.SessionUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {
	private final Integer FIX_MEMORY_RECORD_SIZE = 7;
	private final ImageFileRepository imageFileRepository;
	private final SessionUtil sessionUtil;
	private final MemberRepository memberRepository;
	private final RecordCategoryRepository recordCategoryRepository;
	private final RecordColorRepository recordColorRepository;
	private final RecordIconRepository recordIconRepository;
	private final RecordRepository recordRepository;
	private final ImageFileService imageFileService;
	private final CommentRepository commentRepository;

	@Transactional
	public WriteRecordResponseDto writeRecord(WriteRecordRequestDto writeRecordRequestDto,
			List<MultipartFile> attachments) {

		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		RecordCategory recordCategory = recordCategoryRepository.findById(writeRecordRequestDto.getRecordCategoryId())
				.orElseThrow(() -> new RecordCategoryNotFoundException("카테고리 정보를 찾을 수 없습니다."));

		RecordColor recordColor = recordColorRepository.findByName(writeRecordRequestDto.getColorName())
				.orElseThrow(() -> new RecordColorNotFoundException("컬러 정보를 찾을 수 없습니다."));

		RecordIcon recordIcon = recordIconRepository.findByName(writeRecordRequestDto.getIconName())
				.orElseThrow(() -> new RecordIconNotFoundException("아이콘 정보를 찾을 수 없습니다."));

		Record saveRecord = recordRepository.save(
				Record.of(
						writeRecordRequestDto,
						recordCategory,
						member,
						recordColor,
						recordIcon
				)
		);
		log.info("저장한 레코드 ID : {}", saveRecord.getId());

		if (!imageFileService.isEmptyFile(attachments)) {
			List<String> urls = imageFileService.saveAttachmentFiles(RefType.RECORD, saveRecord.getId(), attachments);
			log.info("저장된 이미지 urls : {}", urls);
		}

		return WriteRecordResponseDto.builder()
				.recordId(saveRecord.getId())
				.build();
	}

	@Transactional(readOnly = true)
	public RecordDetailResponseDto getDetailRecord(Long recordId) {
		Record record = recordRepository.findById(recordId)
				.orElseThrow(() -> new RecordNotFoundException("레코드 정보를 찾을 수 없습니다."));

		List<String> findImageFileUrls = imageFileRepository.findAllByRefTypeAndRefId(
						RefType.RECORD,
						recordId
				).stream()
				.map(ImageFile::getDownloadUrl)
				.collect(Collectors.toList());
		log.info("조회한 이미지 파일 URL : {}", findImageFileUrls);

		return RecordDetailResponseDto.builder()
				.recordId(record.getId())
				.categoryId(record.getRecordCategory().getId())
				.categoryName(record.getRecordCategory().getName())
				.title(record.getTitle())
				.content(record.getContent())
				.writer(record.getWriter().getNickname())
				.colorName(record.getRecordColor().getName())
				.iconName(record.getRecordIcon().getName())
				.createdAt(record.getCreatedAt())
				.imageUrls(findImageFileUrls)
				.build();
	}

	@Transactional(readOnly = true)
	public MemoryRecordResponseDto getMemoryRecordList(Integer pageNum) {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		PageRequest pageRequest = PageRequest.of(
				pageNum,
				FIX_MEMORY_RECORD_SIZE,
				Sort.by(Sort.Direction.DESC, "createdAt")
		);

		List<List<Comment>> commentList = new ArrayList<>();

		Slice<Record> recordSlice = recordRepository.findByWriterAndCreatedAtBefore(
				member,
				LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
				pageRequest
		).map(
				record -> {
					List<Comment> list = commentRepository
							.findTop5ByRecordAndParentCommentIsNullOrderByCreatedAtDesc(record);

					commentList.add(list);

					return record;
				}
		);

		return MemoryRecordResponseDto.builder()
				.memoryRecordSlice(recordSlice)
				.commentList(commentList)
				.build();
    }
    
  @Transactional
  public void deleteRecord(Long recordId) {
		Record record = recordRepository.findByIdFetchWriter(recordId)
				.orElseThrow(() -> new RecordNotFoundException("레코드 정보를 찾을 수 없습니다."));

		if (record.getWriter().getId() != member.getId()) {
			throw new NotMatchLoginUserWithRecordWriterException("로그인 한 사용자와 글 작성자가 일치하지 않습니다.");
		}

		recordRepository.delete(record);
	}
}
