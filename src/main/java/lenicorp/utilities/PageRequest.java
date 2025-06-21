package lenicorp.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class PageRequest
{
    private int page;
    private int size;

    public static PageRequest of(int page, int size)
    {
        return new PageRequest(page, size);
    }
}
