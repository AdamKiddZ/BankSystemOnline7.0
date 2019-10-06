package pojo;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor()
@NoArgsConstructor
@ToString

public class Log implements Serializable {

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
