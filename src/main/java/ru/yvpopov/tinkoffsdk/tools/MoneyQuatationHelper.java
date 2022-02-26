package ru.yvpopov.tinkoffsdk.tools;

import java.math.BigDecimal;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Quotation;


public class MoneyQuatationHelper {
    
    public enum CurrencyEnum {
        RUB("RUB"),
        USD("USD"),
        EUR("EUR");

    private final String text;
        
    CurrencyEnum(final String text) {
        this.text = text;
    }
        
    @Override
    public String toString() {
        return text;
    }        
    }

    public static MoneyValue BigDecimaltoMoneyValue(BigDecimal value, CurrencyEnum currency) {
        return MoneyValue.newBuilder()
                .setCurrency(currency.toString())
                .setUnits(value != null 
                        ? value.longValue() 
                        : 0)
                .setNano(value != null 
                        ? value.remainder(BigDecimal.ONE)
                                .multiply(BigDecimal.valueOf(1_000_000_000))
                                .intValue() 
                        : 0)
                .build();
    }
    
    public static BigDecimal MoneyValuetoBigDecimal(MoneyValue value) {
        return value.getUnits() == 0 && value.getNano() == 0 
                ? BigDecimal.ZERO 
                : BigDecimal.valueOf(value.getUnits())
                        .add(BigDecimal.valueOf(value.getNano(), 9));
    }

    public static Quotation BigDecimaltoQuotation(BigDecimal value) {
        return Quotation.newBuilder()
                .setUnits(value != null 
                        ? value.longValue() 
                        : 0)
                .setNano(value != null 
                        ? value.remainder(BigDecimal.ONE)
                                .multiply(BigDecimal.valueOf(1_000_000_000))
                                .intValue() 
                        : 0)
                .build();
    }
    
    public static BigDecimal QuotationtoBigDecimal(Quotation value) {
        return value.getUnits() == 0 && value.getNano() == 0 
                ? BigDecimal.ZERO 
                : BigDecimal.valueOf(value.getUnits())
                        .add(BigDecimal.valueOf(value.getNano(), 9));
    }
    
    public static int Compare(Quotation x, Quotation y) {
        return ru.yvpopov.tools.Helper.Compare(x.getUnits(), x.getNano(), y.getUnits(), y.getNano());
    }

    public static int Compare(MoneyValue x, MoneyValue y) {
        return ru.yvpopov.tools.Helper.Compare(x.getUnits(), x.getNano(), y.getUnits(), y.getNano());
    }
    
    
}
