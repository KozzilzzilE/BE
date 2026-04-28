package com.pocketco.domain.bookmark.application;

import com.pocketco.domain.bookmark.dto.BookmarkResponseDTO.*;
import com.pocketco.domain.bookmark.entity.Bookmark;
import com.pocketco.domain.bookmark.exception.BookmarkAlreadyExistsException;
import com.pocketco.domain.bookmark.exception.BookmarkNotFoundException;
import com.pocketco.domain.bookmark.repository.BookmarkRepository;
import com.pocketco.domain.problem.entity.Problem;
import com.pocketco.domain.problem.exception.ProblemHandler;
import com.pocketco.domain.problem.repository.ProblemRepository;
import com.pocketco.domain.user.entity.User;
import com.pocketco.domain.user.exception.UserNotFoundException;
import com.pocketco.domain.user.repository.UserRepository;
import com.pocketco.global.common.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;

    public BookmarkToggleResponse addBookmark(Long userId, Long problemId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemHandler(ErrorStatus.PROBLEM_NOT_FOUND));

        if (bookmarkRepository.existsByUserAndProblem(user, problem)) {
            throw new BookmarkAlreadyExistsException();
        }

        bookmarkRepository.save(Bookmark.builder().user(user).problem(problem).build());
        return BookmarkToggleResponse.builder().bookmarked(true).build();
    }

    public BookmarkToggleResponse removeBookmark(Long userId, Long problemId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemHandler(ErrorStatus.PROBLEM_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByUserAndProblem(user, problem)
                .orElseThrow(BookmarkNotFoundException::new);

        bookmarkRepository.delete(bookmark);
        return BookmarkToggleResponse.builder().bookmarked(false).build();
    }

    @Transactional(readOnly = true)
    public List<BookmarkListResponse> getMyBookmarks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Bookmark> bookmarks = bookmarkRepository.findByUserOrderByCreatedAtDesc(user);

        return bookmarks.stream()
                .map(b -> {
                    Problem p = b.getProblem();
                    String displayName = switch (p.getDifficulty().toUpperCase()) {
                        case "EASY" -> "쉬움";
                        case "NORMAL" -> "보통";
                        case "HARD" -> "어려움";
                        default -> "미정";
                    };
                    return BookmarkListResponse.builder()
                            .problemId(p.getId())
                            .title(p.getTitle())
                            .difficulty(p.getDifficulty())
                            .difficultyDisplayName(displayName)
                            .bookmarkCount(bookmarkRepository.countByProblem(p))
                            .build();
                }).toList();
    }
}