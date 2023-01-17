package functional;


import com.bitvavo.api.OrderCommandServiceImpl;
import com.bitvavo.orderbook.PriceTimeAskComparator;
import com.bitvavo.orderbook.PriceTimeBidComparator;
import com.bitvavo.orderbook.entity.Order;
import com.bitvavo.orderbook.entity.OrderAction;
import com.bitvavo.orderbook.entity.OrderBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

class OrderBookTest {

    @BeforeEach
    void initTests(){
        OrderBook.setBidTree(new TreeSet<>(new PriceTimeBidComparator()));
        OrderBook.setAskTree(new TreeSet<>(new PriceTimeAskComparator()));

    }
    @Test
    void orderBookSideBuyShouldBeSortedByPriceDescendingOrder() {
        //GIVEN
        Order order1 = new Order("1001", OrderAction.BID, 98, 1900, Instant.now());
        Order order2 = new Order("1002", OrderAction.BID, 100, 1100, Instant.now());
        Order order3 = new Order("1003", OrderAction.BID, 106, 1200, Instant.now());
        OrderCommandServiceImpl orderCommandService = new OrderCommandServiceImpl();

        //WHEN
        orderCommandService.sendOrder(order1);
        orderCommandService.sendOrder(order2);
        orderCommandService.sendOrder(order3);

        //THEN
        Assertions.assertEquals(3, OrderBook.getBidTree().size());
        Assertions.assertEquals("1003", OrderBook.getBidTree().first().getOrderId());
        Assertions.assertEquals(106, OrderBook.getBidTree().first().getPrice());

        Assertions.assertEquals("1001", OrderBook.getBidTree().last().getOrderId());
        Assertions.assertEquals(98, OrderBook.getBidTree().last().getPrice());
    }

    @Test
    void orderBookSideSellShouldBeSortedAscendingBasedOnPrice() {
        //GIVEN
        Order order1 = new Order("1001", OrderAction.ASK, 98, 1900, Instant.now());
        Order order2 = new Order("1002", OrderAction.ASK, 100, 1100, Instant.now());
        Order order3 = new Order("1003", OrderAction.ASK, 106, 1200, Instant.now());
        OrderCommandServiceImpl orderCommandService = new OrderCommandServiceImpl();

        //WHEN
        orderCommandService.sendOrder(order1);
        orderCommandService.sendOrder(order2);
        orderCommandService.sendOrder(order3);

        //THEN
        Assertions.assertEquals(3, OrderBook.getAskTree().size());
        Assertions.assertEquals("1001", OrderBook.getAskTree().first().getOrderId());
        Assertions.assertEquals(98, OrderBook.getAskTree().first().getPrice());

        Assertions.assertEquals("1003", OrderBook.getAskTree().last().getOrderId());
        Assertions.assertEquals(106, OrderBook.getAskTree().last().getPrice());

    }


    @Test
    void orderBookSortedByOldestToNewestOrdersTimestamp() {
        //GIVEN
        Order order1 = new Order("1001", OrderAction.BID, 98, 1900, Instant.now());
        Order order2 = new Order("1002", OrderAction.BID, 98, 1100,
            Instant.now().plus(3, ChronoUnit.HOURS));

        Order order5 = new Order("1005", OrderAction.ASK, 106, 1100, Instant.now());
        Order order6 = new Order("1006", OrderAction.ASK, 106, 1200,
            Instant.now().minus(3, ChronoUnit.HOURS));
        OrderCommandServiceImpl orderCommandService = new OrderCommandServiceImpl();

        //WHEN
        orderCommandService.sendOrder(order1);
        orderCommandService.sendOrder(order2);
        orderCommandService.sendOrder(order5);
        orderCommandService.sendOrder(order6);

        //THEN
        Assertions.assertEquals("1001", OrderBook.getBidTree().first().getOrderId());
        Assertions.assertEquals("1002", OrderBook.getBidTree().last().getOrderId());

        Assertions.assertEquals("1006", OrderBook.getAskTree().first().getOrderId());
        Assertions.assertEquals("1005", OrderBook.getAskTree().last().getOrderId());
    }
}
