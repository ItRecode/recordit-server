package com.recordit.server.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.recordit.server.dto.record.ModifyRecordRequestDto;
import com.recordit.server.dto.record.RandomRecordRequestDto;
import com.recordit.server.dto.record.RandomRecordResponseDto;
import com.recordit.server.dto.record.RecordByDateRequestDto;
import com.recordit.server.dto.record.RecordByDateResponseDto;
import com.recordit.server.dto.record.RecordDetailResponseDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.dto.record.WriteRecordResponseDto;
import com.recordit.server.dto.record.memory.MemoryRecordRequestDto;
import com.recordit.server.dto.record.memory.MemoryRecordResponseDto;
import com.recordit.server.dto.record.mix.MixRecordDto;
import com.recordit.server.dto.record.mix.MixRecordResponseDto;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.record.FixRecordNotExistException;
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
import com.recordit.server.util.DateTimeUtil;
import com.recordit.server.util.SessionUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordService {

	private final int MIX_RECORD_COMMENT_SIZE = 10;
	private final long FIX_RECORD_PK_VALUE = 31L;
	private final int FIRST_PAGE = 0;

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
	public RecordByDateResponseDto getRecordBy(RecordByDateRequestDto recordByDateRequestDto) {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		Page<Record> findRecords = recordRepository.findAllByWriterAndCreatedAtBetweenOrderByCreatedAtDesc(
				member,
				DateTimeUtil.getStartOfDay(recordByDateRequestDto.getDate()),
				DateTimeUtil.getEndOfDay(recordByDateRequestDto.getDate()),
				PageRequest.of(
						recordByDateRequestDto.getPage(),
						recordByDateRequestDto.getSize(),
						Sort.Direction.DESC,
						"createdAt"
				)
		);

		LinkedHashMap<Record, Long> recordToNumOfComment = new LinkedHashMap<>();
		for (Record record : findRecords) {
			recordToNumOfComment.put(
					// key
					record,
					// value
					commentRepository.countByRecordAndParentCommentIsNull(record)
			);
		}

		return RecordByDateResponseDto.builder()
				.records(findRecords)
				.recordToNumOfComments(recordToNumOfComment)
				.build();
	}

	@Transactional(readOnly = true)
	public MemoryRecordResponseDto getMemoryRecords(MemoryRecordRequestDto memoryRecordRequestDto) {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		PageRequest pageRequest = PageRequest.of(
				memoryRecordRequestDto.getMemoryRecordPage(),
				memoryRecordRequestDto.getMemoryRecordSize(),
				Sort.by(Sort.Direction.DESC, "createdAt")
		);

		Page<Record> findRecords = recordRepository.findByWriterFetchAllCreatedAtBefore(
				member,
				DateTimeUtil.getStartOfToday(),
				pageRequest
		);

		Map<Record, List<Comment>> recordToComments = new LinkedHashMap<>();
		for (Record findRecord : findRecords) {
			recordToComments.put(
					// key
					findRecord,
					// value
					commentRepository.findAllByRecord(
							findRecord,
							PageRequest.of(
									FIRST_PAGE,
									memoryRecordRequestDto.getSizeOfCommentPerRecord(),
									Sort.by(Sort.Direction.DESC, "createdAt")
							)
					)
			);
		}

		return MemoryRecordResponseDto.builder()
				.memoryRecords(findRecords)
				.recordToComments(recordToComments)
				.build();
	}

	@Transactional
	public void deleteRecord(Long recordId) {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		Record record = recordRepository.findByIdFetchWriter(recordId)
				.orElseThrow(() -> new RecordNotFoundException("레코드 정보를 찾을 수 없습니다."));

		if (record.getWriter().getId() != member.getId()) {
			throw new NotMatchLoginUserWithRecordWriterException("로그인 한 사용자와 글 작성자가 일치하지 않습니다.");
		}

		imageFileService.delete(RefType.RECORD, recordId);
		recordRepository.delete(record);
	}

	@Transactional
	public Long modifyRecord(
			Long recordId,
			ModifyRecordRequestDto modifyRecordRequestDto,
			List<MultipartFile> attachments
	) {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		Record record = recordRepository.findByIdFetchWriter(recordId)
				.orElseThrow(() -> new RecordNotFoundException("레코드 정보를 찾을 수 없습니다."));

		RecordColor recordColor = recordColorRepository.findByName(modifyRecordRequestDto.getColorName())
				.orElseThrow(() -> new RecordColorNotFoundException("컬러 정보를 찾을 수 없습니다."));

		RecordIcon recordIcon = recordIconRepository.findByName(modifyRecordRequestDto.getIconName())
				.orElseThrow(() -> new RecordIconNotFoundException("아이콘 정보를 찾을 수 없습니다."));

		if (record.getWriter().getId() != member.getId()) {
			throw new NotMatchLoginUserWithRecordWriterException("로그인 한 사용자와 글 작성자가 일치하지 않습니다.");
		}

		if (!imageFileService.isEmptyFile(attachments)) {
			List<String> urls = imageFileService.saveAttachmentFiles(RefType.RECORD, record.getId(), attachments);
			log.info("저장된 이미지 urls : {}", urls);
		}

		if (modifyRecordRequestDto.getDeleteImages() != null) {
			imageFileService.deleteAttachmentFiles(
					RefType.RECORD,
					recordId,
					modifyRecordRequestDto.getDeleteImages()
			);
		}

		return record.modify(modifyRecordRequestDto, recordColor, recordIcon);
	}

	@Transactional
	public List<RandomRecordResponseDto> getRandomRecord(
			RandomRecordRequestDto randomRecordRequestDto
	) {
		if (!recordRepository.existsById(randomRecordRequestDto.getRecordCategoryId())) {
			throw new RecordCategoryNotFoundException("카테고리 정보를 찾을 수 없습니다.");
		}

		List<Record> recordList = recordRepository.findRandomRecordByRecordCategoryId(
				randomRecordRequestDto.getSize(),
				randomRecordRequestDto.getRecordCategoryId()
		);

		return recordList.stream()
				.map(record -> RandomRecordResponseDto.of(
						record,
						commentRepository.countByRecordId(record.getId())
				)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public MixRecordResponseDto getMixRecords() {
		Record fixRecord = recordRepository.findById(FIX_RECORD_PK_VALUE)
				.orElseThrow(() -> new FixRecordNotExistException("서버에 고정 레코드가 존재하지 않습니다."));

		List<MixRecordDto> commentList = commentRepository.findByRecord(fixRecord);

		Random random = new Random();
		List<MixRecordDto> randomCommentList = new ArrayList<>();

		if (commentList.size() != 0) {
			for (int i = 0; i < MIX_RECORD_COMMENT_SIZE; i++) {
				randomCommentList.add(commentList.get(random.nextInt(commentList.size())));
			}
		}

		return MixRecordResponseDto.builder()
				.mixRecordDto(randomCommentList)
				.build();
	}
}
