package com.leman.contentmanagementapi.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.leman.contentmanagementapi.dto.request.ArticleCreateRequest;
import com.leman.contentmanagementapi.dto.request.ArticleFilterRequest;
import com.leman.contentmanagementapi.dto.request.ArticleUpdateRequest;
import com.leman.contentmanagementapi.dto.response.ArticleResponse;
import com.leman.contentmanagementapi.dto.response.PageableResponse;
import com.leman.contentmanagementapi.security.UserPrincipal;
import com.leman.contentmanagementapi.service.ArticleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleResponse> create(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @RequestBody @Valid ArticleCreateRequest request) {
        return ResponseEntity.status(CREATED).body(articleService.createArticle(request, userPrincipal.getUser()));
    }

    @PostMapping("/search")
    public ResponseEntity<PageableResponse<ArticleResponse>> getAll(ArticleFilterRequest request) {
        return ResponseEntity.ok(articleService.findAllArticles(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(articleService.findArticleById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleResponse> update(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @PathVariable @Positive Long id,
                                                  @RequestBody @Valid ArticleUpdateRequest request) {
        return ResponseEntity.ok(articleService.updateArticle(id, request, userPrincipal.getUser().getId()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable @Positive Long id) {
        articleService.publishArticle(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PathVariable @Positive Long id) {
        articleService.deleteArticle(id, userPrincipal.getUser());

        return ResponseEntity.noContent().build();
    }

}
