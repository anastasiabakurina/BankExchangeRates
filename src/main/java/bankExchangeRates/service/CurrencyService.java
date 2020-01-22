package bankExchangeRates.service;

import lombok.val;
import org.hibernate.Session;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.hibernate.query.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import bankExchangeRates.util.HibernateUtil;
import bankExchangeRates.model.Currency;
import java.io.IOException;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

public class CurrencyService {
    private String date = null;
    private String elementCurrency = null;
    private String elementCurrencyBuy = null;
    private String elementCurrencySale = null;
    private String title = null;
    private String address = null;
    private Document document = null;
    private Session session = HibernateUtil.getSession();
    public static Logger logger = Logger.getLogger(CurrencyService.class.getName());

    private Map<String, String> mapCurrencySale = new HashMap<>();

    public void startParse() {
        try {
            document = Jsoup.connect("https://www.bnb.by/kursy-valyut/fizicheskim-litsam/").timeout(20000).get();
            LogManager.getLogManager().readConfiguration(CurrencyService.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            logger.warning("Timeout!!!");
            e.printStackTrace();
        }

        Elements classElements = document.getElementsByAttributeValue("class", "tr-info");
        Elements elementsDate = document.getElementsByAttributeValue("name", "request[TIME]");//.getElementsByAttribute("input");//.getElementsByTag("input");
        for (Element inputElement : elementsDate) {
            date = inputElement.attr("value");
        }

        logger.info("date: " + date);
        for (Element element : classElements) {
            Element elementCenter = element.child(0);
            title = elementCenter.child(0).text();
            address = elementCenter.child(1).text();
            logger.info("address/title " + address + " " + title);
            Element elementCurrencyClass = element.child(2);

            Elements es = elementCurrencyClass.getElementsByAttributeValue("class", "currency_wrap");
            for (Element element1 : es) {
                Element element3 = element1.child(0);
                Element elementValueCurrency = element3.child(1);

                Elements get = elementValueCurrency.getElementsByTag("tr");
                for (Element element2 : get) {
                    elementCurrency = element2.child(0).text();
                    elementCurrencyBuy = element2.child(1).text();
                    elementCurrencySale = element2.child(2).text();
                    logger.info("values=" + elementCurrency + " " + elementCurrencyBuy + " " + elementCurrencySale);

                    if ("USD".equals(elementCurrency)) {
                        mapCurrencySale.put(address, elementCurrencySale);
                    }

                    val currency = Currency.builder()
                            .officeTitle(title)
                            .officeAddress(address)
                            .elementCurrency(elementCurrency)
                            .elementCurrencyBuy(elementCurrencyBuy)
                            .elementCurrencySale(elementCurrencySale)
                            .date(date)
                            .build();

                    session.save(currency);
                    session.getTransaction().begin();
                    session.persist(currency);
                    session.getTransaction().commit();
                }
            }
        }
    }

    public String getMinCurrency() {
        String min = Collections.min(mapCurrencySale.values());
        return min;
    }

    public String getAddressMinCurrency() {
        List<String> listAddresses = new ArrayList<>();
        for (Map.Entry<String, String> entry : mapCurrencySale.entrySet()) {
            if (entry.getValue().equals(getMinCurrency())) {
                listAddresses.add("\n" + entry.getKey());
            }
        }
        String addresses = StringUtils.substringBetween(listAddresses.toString(), "[", "]");
        logger.info("The lowest currency: " + getMinCurrency());
        logger.info("The addresses: " + addresses);
        return addresses;
    }

    public void checkDBTable() {
        session.getTransaction().begin();
        Query query = session.createQuery("FROM Currency");
        List<Currency> list = (List<Currency>) query.list();
        session.getTransaction().commit();

        if (list != null && !list.isEmpty()) {
            for (Currency currency : list) {
                logger.info("what in db: " + currency);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyService service = (CurrencyService) o;
        return Objects.equals(mapCurrencySale, service.mapCurrencySale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapCurrencySale);
    }
}
