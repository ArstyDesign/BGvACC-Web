package com.bgvacc.web.responses.paging;

import java.util.List;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @param <T> type of elements
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class PaginationResponse<T> {

  private List<T> items;
  private int currentPage;
  private int totalPages;
  private boolean isFirst;
  private boolean isLast;

  public PaginationResponse(List<T> items, int currentPage, int totalPages) {
    this.items = items;
    this.currentPage = currentPage;
    this.totalPages = totalPages;
    this.isFirst = currentPage == 1;
    this.isLast = currentPage == totalPages;
  }
}
