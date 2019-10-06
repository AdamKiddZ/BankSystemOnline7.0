package pojo;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor()
@NoArgsConstructor
@ToString

public class Log {

    private Integer logid;

    @NonNull
    private String logtype;

    @NonNull
    private BigDecimal amount;

    @NonNull
    private User user;

    @NonNull
    private Timestamp logtime;
}
