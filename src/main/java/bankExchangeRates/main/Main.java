package bankExchangeRates.main;

import bankExchangeRates.service.Service;

public class Main{
    public static void main(String[] args) {
        Service service = new Service();
        service.startParse();
        service.checkDBTable();
        System.out.println("---" + service.getAddressMinCurrency());
    }
}
