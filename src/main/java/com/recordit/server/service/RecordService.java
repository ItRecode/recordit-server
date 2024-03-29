package com.recordit.server.service;

import static com.recordit.server.util.DateTimeUtil.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.recordit.server.dto.record.RecentOneRecordResponseDto;
import com.recordit.server.dto.record.RecentRecordRequestDto;
import com.recordit.server.dto.record.RecentRecordResponseDto;
import com.recordit.server.dto.record.RecordBySearchRequestDto;
import com.recordit.server.dto.record.RecordBySearchResponseDto;
import com.recordit.server.dto.record.RecordDetailResponseDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.dto.record.WriteRecordResponseDto;
import com.recordit.server.dto.record.WrittenRecordDayRequestDto;
import com.recordit.server.dto.record.WrittenRecordDayResponseDto;
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
	@CacheEvict(value = "RecordAllCount", allEntries = true)
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
	public RecentOneRecordResponseDto getTodayRecentOneRecord() {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		Record record = recordRepository.findFirstByWriterAndCreatedAtBetweenOrderByCreatedAtDesc(
				member,
				getStartOfDay(LocalDate.now()),
				getEndOfDay(LocalDate.now())
		).orElseThrow(() -> new RecordNotFoundException("오늘 쓴 레코드를 찾을 수 없습니다."));

		return RecentOneRecordResponseDto.of(record);
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

		Page<Record> findRecords = lookupRecordsByDate(memoryRecordRequestDto, member, pageRequest);

		Map<Record, List<Comment>> recordToComments = new LinkedHashMap<>();
		for (Record findRecord : findRecords) {
			recordToComments.put(
					// key
					findRecord,
					// value
					commentRepository.findAllByRecordAndParentCommentIsNull(
							findRecord,
							PageRequest.of(
									FIRST_PAGE,
									memoryRecordRequestDto.getSizeOfCommentPerRecord(),
									Sort.by(Sort.Direction.DESC, "createdAt")
							)
					)
			);
		}

		return MemoryRecordResponseDto.of(findRecords, recordToComments);
	}

	@Transactional
	@CacheEvict(value = "RecordAllCount", allEntries = true)
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

	@Transactional(readOnly = true)
	public List<RandomRecordResponseDto> getRandomRecord(
			RandomRecordRequestDto randomRecordRequestDto
	) {
		if (!recordCategoryRepository.existsById(randomRecordRequestDto.getRecordCategoryId())) {
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

		List<MixRecordDto> commentList = commentRepository.findByRecord(fixRecord).stream()
				.map(comment -> MixRecordDto.builder()
						.commentId(comment.getId())
						.colorName(comment.getRecord().getRecordColor().getName())
						.iconName(comment.getRecord().getRecordIcon().getName())
						.commentContent(comment.getContent())
						.recordId(comment.getRecord().getId())
						.build()
				).collect(Collectors.toList());

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

	@Transactional(readOnly = true)
	public Page<RecentRecordResponseDto> getRecentRecord(
			RecentRecordRequestDto recentRecordRequestDto
	) {
		Page<Record> recordPage = recordRepository.findAllByCreatedAtBeforeFetchRecordIconAndRecordColor(
				PageRequest.of(
						recentRecordRequestDto.getPage(),
						recentRecordRequestDto.getSize(),
						Sort.Direction.DESC,
						"createdAt"
				),
				recentRecordRequestDto.getDateTime()
		);

		return recordPage.map(
				record -> RecentRecordResponseDto.of(
						record,
						commentRepository.countByRecordId(record.getId())
				)
		);
	}

	@Transactional(readOnly = true)
	public RecordBySearchResponseDto getRecordsBySearch(
			RecordBySearchRequestDto recordBySearchRequestDto
	) {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		PageRequest pageRequest = PageRequest.of(
				recordBySearchRequestDto.getPage(),
				recordBySearchRequestDto.getSize(),
				Sort.by(Sort.Direction.DESC, "createdAt")
		);

		Page<Record> findRecords = recordRepository.findByWriterAndTitleContaining(
				member,
				recordBySearchRequestDto.getSearchKeyword(),
				pageRequest
		);

		Map<Record, Long> recordToNumOfComment = new LinkedHashMap<>();
		for (Record findRecord : findRecords) {
			recordToNumOfComment.put(
					// key
					findRecord,
					// value
					commentRepository.countByRecordAndParentCommentIsNull(findRecord)
			);
		}

		return RecordBySearchResponseDto.builder()
				.records(findRecords)
				.recordToNumOfComments(recordToNumOfComment)
				.build();
	}

	public WrittenRecordDayResponseDto getWrittenRecordDays(
			WrittenRecordDayRequestDto writtenRecordDayRequestDto
	) {
		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("세션에서 찾은 사용자 ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("회원 정보를 찾을 수 없습니다."));

		LocalDateTime start = getFirstDayOfMonth(writtenRecordDayRequestDto.getYearMonth());
		LocalDateTime end = getLastDayOfMonth(writtenRecordDayRequestDto.getYearMonth());

		TreeSet<Integer> writtenRecordDays =
				recordRepository.findAllByWriterAndCreatedAtBetween(member, start, end)
						.stream()
						.map(record -> record.getCreatedAt().getDayOfMonth())
						.collect(Collectors.toCollection(TreeSet::new));

		return WrittenRecordDayResponseDto.of(writtenRecordDays);
	}

	@Cacheable(value = "RecordAllCount")
	public long getRecordAllCount() {
		return recordRepository.count();
	}

	private Page<Record> lookupRecordsByDate(
			MemoryRecordRequestDto memoryRecordRequestDto,
			Member member,
			PageRequest pageRequest
	) {
		if (memoryRecordRequestDto.getDate() != null) {
			return recordRepository.findAllByWriterAndCreatedAtBetweenOrderByCreatedAtDesc(
					member,
					getStartOfDay(memoryRecordRequestDto.getDate()),
					getEndOfDay(memoryRecordRequestDto.getDate()),
					pageRequest
			);
		}

		return recordRepository.findByWriterFetchAllCreatedAtBefore(
				member,
				DateTimeUtil.getStartOfToday(),
				pageRequest
		);
	}
}
