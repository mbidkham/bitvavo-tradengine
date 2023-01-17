package functional;

import com.bitvavo.api.IOrderCommandService;
import com.bitvavo.api.OrderCommandServiceImpl;
import com.bitvavo.orderbook.PriceTimeAskComparator;
import com.bitvavo.orderbook.PriceTimeBidComparator;
import com.bitvavo.orderbook.entity.Order;
import com.bitvavo.orderbook.entity.OrderAction;
import com.bitvavo.orderbook.entity.OrderBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.util.TreeSet;

class MatchingEngineTest {

    private IOrderCommandService orderCommandService;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void init() {
        orderCommandService = new OrderCommandServiceImpl();
        OrderBook.setBidTree(new TreeSet<>(new PriceTimeBidComparator()));
        OrderBook.setAskTree(new TreeSet<>(new PriceTimeAskComparator()));
    }

    @Test
    void noMatchedOrdersAvailableShouldJustPrintOrderBook() {

        //GIVEN
        Order order1 = new Order("10000", OrderAction.BID, 98, 25_500, Instant.now());
        Order order2 = new Order("10005", OrderAction.ASK, 105, 20_000, Instant.now());
        Order order3 = new Order("10001", OrderAction.ASK, 100, 500, Instant.now());
        Order order4 = new Order("10002", OrderAction.ASK, 100, 10_000, Instant.now());
        Order order5 = new Order("10003", OrderAction.BID, 99, 50_000, Instant.now());
        Order order6 = new Order("10004", OrderAction.ASK, 103, 100, Instant.now());

        //WHEN
        orderCommandService.sendOrder(order1);
        orderCommandService.sendOrder(order2);
        orderCommandService.sendOrder(order3);
        orderCommandService.sendOrder(order4);
        orderCommandService.sendOrder(order5);
        orderCommandService.sendOrder(order6);
        OrderBook.printOrderBook();

        //THEN
        Assertions.assertEquals(2, OrderBook.getBidTree().size());
        Assertions.assertEquals(4, OrderBook.getAskTree().size());

    }

    @Test
    void buyOrderCanMatchToAskItemsShouldGetTrades(){
        System.setOut(new PrintStream(outContent));

        //GIVEN
        Order order1 = new Order("10000", OrderAction.BID, 98, 25_500, Instant.now());
        Order order2 = new Order("10005", OrderAction.ASK, 105, 20_000, Instant.now());
        Order order3 = new Order("10001", OrderAction.ASK, 100, 500, Instant.now());
        Order order4 = new Order("10002", OrderAction.ASK, 100, 10_000, Instant.now());
        Order order5 = new Order("10003", OrderAction.BID, 99, 50_000, Instant.now());
        Order order6 = new Order("10004", OrderAction.ASK, 103, 100, Instant.now());
        Order newOrder = new Order("10006", OrderAction.BID, 105, 16_000, Instant.now());

        //WHEN
        orderCommandService.sendOrder(order1);
        orderCommandService.sendOrder(order2);
        orderCommandService.sendOrder(order3);
        orderCommandService.sendOrder(order4);
        orderCommandService.sendOrder(order5);
        orderCommandService.sendOrder(order6);
        orderCommandService.sendOrder(newOrder);
        OrderBook.printOrderBook();

        //THEN
        Assertions.assertEquals(
            "trade 10006 10001 100 500\n" +
            "trade 10006 10002 100 10000\n" +
            "trade 10006 10004 103 100\n" +
            "trade 10006 10005 105 5400\n" +
            "Buyers      Sellers\n" +
            "50000       99     | 105    14600      \n" +
            "25500       98     |                   \n", outContent.toString());
    }

    @Test
    void sellOrderMatchShouldGenerateTrade(){
        System.setOut(new PrintStream(outContent));

        //GIVEN
        Order order1 = new Order("10000", OrderAction.BID, 98, 25_500, Instant.now());
        Order order2 = new Order("10005", OrderAction.ASK, 105, 20_000, Instant.now());
        Order order3 = new Order("10001", OrderAction.ASK, 100, 500, Instant.now());
        Order order4 = new Order("10002", OrderAction.ASK, 100, 10_000, Instant.now());
        Order order5 = new Order("10003", OrderAction.BID, 99, 50_000, Instant.now());
        Order order6 = new Order("10004", OrderAction.ASK, 103, 100, Instant.now());
        Order newOrder = new Order("10006", OrderAction.ASK, 89, 16_000, Instant.now());

        //WHEN
        orderCommandService.sendOrder(order1);
        orderCommandService.sendOrder(order2);
        orderCommandService.sendOrder(order3);
        orderCommandService.sendOrder(order4);
        orderCommandService.sendOrder(order5);
        orderCommandService.sendOrder(order6);
        orderCommandService.sendOrder(newOrder);
        OrderBook.printOrderBook();

        //THEN
        Assertions.assertEquals(
            "trade 10006 10003 99 16000\n" +
            "Buyers      Sellers\n" +
            "34000       99     | 100    500        \n" +
            "25500       98     | 100    10000      \n" +
            "                   | 103    100        \n" +
            "                   | 105    20000      \n", outContent.toString());

    }
}
