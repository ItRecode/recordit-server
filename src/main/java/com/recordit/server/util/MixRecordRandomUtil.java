package com.recordit.server.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.recordit.server.domain.Comment;

public class MixRecordRandomUtil {
	private static Random random = new Random();

	public static List<Comment> getRandomCommentList(List<Comment> commentList, Integer commentListSize) {
		List<Comment> randomCommentList = new ArrayList<>();

		if (commentList.size() == 0) {
			return randomCommentList;
		}

		for (int i = 0; i < commentListSize; i++) {
			randomCommentList.add(commentList.get(random.nextInt(commentList.size())));
		}

		return randomCommentList;
	}

	/*public static List<Record> getRandomRecordList(List<Record> recordList, Long recordListSize) {
		List<Record> randomRecordList = new ArrayList<>();

		for (int i = 0; i < recordListSize; i++) {
			randomRecordList.add(recordList.get(random.nextInt(recordList.size())));
		}

		return randomRecordList;
	}*/
}
