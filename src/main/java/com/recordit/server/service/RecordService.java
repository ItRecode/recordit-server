package com.recordit.server.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.recordit.server.constant.RefType;
import com.recordit.server.domain.ImageFile;
import com.recordit.server.domain.Member;
import com.recordit.server.domain.Record;
import com.recordit.server.domain.RecordCategory;
import com.recordit.server.domain.RecordColor;
import com.recordit.server.domain.RecordIcon;
import com.recordit.server.dto.record.RecordDetailResponseDto;
import com.recordit.server.dto.record.WriteRecordRequestDto;
import com.recordit.server.dto.record.WriteRecordResponseDto;
import com.recordit.server.exception.member.MemberNotFoundException;
import com.recordit.server.exception.record.RecordColorNotFoundException;
import com.recordit.server.exception.record.RecordIconNotFoundException;
import com.recordit.server.exception.record.RecordNotFoundException;
import com.recordit.server.exception.record.category.RecordCategoryNotFoundException;
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
	private final ImageFileRepository imageFileRepository;
	private final SessionUtil sessionUtil;
	private final MemberRepository memberRepository;
	private final RecordCategoryRepository recordCategoryRepository;
	private final RecordColorRepository recordColorRepository;
	private final RecordIconRepository recordIconRepository;
	private final RecordRepository recordRepository;
	private final ImageFileService imageFileService;

	@Transactional
	public WriteRecordResponseDto writeRecord(WriteRecordRequestDto writeRecordRequestDto, List<MultipartFile> files) {

		Long userIdBySession = sessionUtil.findUserIdBySession();
		log.info("???????????? ?????? ????????? ID : {}", userIdBySession);

		Member member = memberRepository.findById(userIdBySession)
				.orElseThrow(() -> new MemberNotFoundException("?????? ????????? ?????? ??? ????????????."));

		RecordCategory recordCategory = recordCategoryRepository.findById(writeRecordRequestDto.getRecordCategoryId())
				.orElseThrow(() -> new RecordCategoryNotFoundException("???????????? ????????? ?????? ??? ????????????."));

		RecordColor recordColor = recordColorRepository.findByName(writeRecordRequestDto.getColorName())
				.orElseThrow(() -> new RecordColorNotFoundException("?????? ????????? ?????? ??? ????????????."));

		RecordIcon recordIcon = recordIconRepository.findByName(writeRecordRequestDto.getIconName())
				.orElseThrow(() -> new RecordIconNotFoundException("????????? ????????? ?????? ??? ????????????."));

		Record record = Record.of(
				writeRecordRequestDto,
				recordCategory,
				member,
				recordColor,
				recordIcon
		);

		Long recordId = recordRepository.save(record).getId();
		log.info("????????? ????????? ID : ", recordId);

		if (files != null) {
			List<String> urls = imageFileService.saveAttachmentFiles(RefType.RECORD, recordId, files);
			log.info("????????? ????????? urls : {}", urls);
		}

		return WriteRecordResponseDto.builder()
				.recordId(recordId)
				.build();
	}

	@Transactional(readOnly = true)
	public RecordDetailResponseDto getDetailRecord(Long recordId) {
		Record record = recordRepository.findById(recordId)
				.orElseThrow(() -> new RecordNotFoundException("????????? ????????? ?????? ??? ????????????."));

		List<String> imageUrls = Collections.emptyList();

		Optional<List<ImageFile>> optionalImageFileList = Optional.of(
				imageFileRepository.findAllByRefTypeAndRefId(
						RefType.RECORD,
						recordId
				)
		);

		if (!optionalImageFileList.isEmpty()) {

			optionalImageFileList.get().stream()
					.forEach(
							(imageFile) -> {
								imageUrls.add(imageFile.getDownloadUrl());
							}
					);

		}

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
				.imageUrls(imageUrls)
				.build();
	}
}
