package lenicorp.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Page<T>
{
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;

    public Page(List<T> content, long totalElements, int page, int size)
    {
        this(content, totalElements, size);
        this.page = page;
    }

    public Page(List<T> content, long totalElements, int size)
    {
        this.content = content;
        this.totalElements = totalElements;
        this.page = 0;
        this.size = size;
        this.totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
    }
}
