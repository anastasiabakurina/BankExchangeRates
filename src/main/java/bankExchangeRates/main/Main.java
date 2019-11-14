package bankExchangeRates.main;

import bankExchangeRates.model.Currency;
import bankExchangeRates.util.HibernateUtil;
import lombok.val;
import org.hibernate.Session;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Logger logger = LoggerFactory.logger(Main.class);
        try (Session session = HibernateUtil.getSession()) {
            String date = null;
            String elementCurrency = null;
            String elementCurrencyBuy = null;
            String elementCurrencySale = null;
            String title = null;
            String address = null;
            Document document = Jsoup.connect("https://www.bnb.by/kursy-valyut/fizicheskim-litsam/").get();
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
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        List<Currency> list = null;
        try (Session session = HibernateUtil.getSession()) {
            session.getTransaction().begin();
            Query query = session.createQuery("FROM Currency");
            list = (List<Currency>) query.list();
            session.getTransaction().commit();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        if (list != null && !list.isEmpty()) {
            for (Currency currency : list) {
                logger.info("what in db: " + currency);
            }
        }
    }
}
