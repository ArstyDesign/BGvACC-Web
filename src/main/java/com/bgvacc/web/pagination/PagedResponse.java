package com.bgvacc.web.pagination;

import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @param <T> the object for the page
 * @since 1.0.0
 */
public class PagedResponse<T> {

  private List<T> content;
  private int currentPage;
  private int totalPages;
  private long totalElements;
  private int pageSize;
  private boolean isLastPage;

  public PagedResponse(List<T> content, int currentPage, int totalPages, long totalElements, int pageSize) {
    this.content = content;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
    this.pageSize = pageSize;
    this.isLastPage = currentPage == totalPages;
  }

  public List<T> getContent() {
    return content;
  }

  public int getCurrentPage() {
    return currentPage;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public int getPageSize() {
    return pageSize;
  }

  public boolean isLastPage() {
    return isLastPage;
  }
}
