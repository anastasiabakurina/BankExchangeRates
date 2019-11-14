package bankExchangeRates.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "currency")
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor(staticName = "create")
public class Currency {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    public int id = 0;

    @Getter
    @Setter
    @Column(name = "Bank_name")
    private String officeTitle;

    @Getter
    @Setter
    @Column(name = "Bank_address")
    private String officeAddress;

    @Getter
    @Setter
    @Column(name = "Currency")
    private String elementCurrency;

    @Getter
    @Setter
    @Column(name = "CurrencyBuy")
    private String elementCurrencyBuy;

    @Getter
    @Setter
    @Column(name = "CurrencySale")
    private String elementCurrencySale;

    @Getter
    @Setter
    @Column(name = "Date")
    private String date;
}
