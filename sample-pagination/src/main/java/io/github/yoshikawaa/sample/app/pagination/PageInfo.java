package io.github.yoshikawaa.sample.app.pagination;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer page;
    private Integer size;
    private String sort;

    public PageInfo(Pageable pageable) {
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.sort = pageable.getSort() == null ? null : pageable.getSort().toString();
    }
}
