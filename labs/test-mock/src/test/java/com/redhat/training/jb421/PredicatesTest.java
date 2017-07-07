package com.redhat.training.jb421;

import static com.redhat.training.jb421.predicates.OrderPredicates.*;
import static com.redhat.training.jb421.predicates.OrderPredicates.validateNumberOfItems;
import static com.redhat.training.jb421.predicates.OrderPredicates.validateZipCode;
import static org.apache.camel.builder.ExchangeBuilder.anExchange;

import org.apache.camel.Exchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.redhat.training.domain.Address;
import com.redhat.training.domain.Customer;
import com.redhat.training.domain.Order;
import com.redhat.training.domain.OrderItem;

public class PredicatesTest extends CamelTestSupport {


	@Test
	public void testValidZipCodePredicates() {
		//Evaluate if the validateZipCode predicate is working using the 25411-2211 postal code
//		Exchange validExchange = anExchange(context).build();
//		Order order = createNewOrder("25411-2211");
//		validExchange.getIn().setBody(order);
//		assertPredicateMatches(validateZipCode(), validExchange);
		Assert.fail("Not implemented yet");
	}

	@Test
	public void testInvalidZipCodePredicates(){
		//Evaluate if the validateZipCode predicate is working using the X7XXX-XX4X postal code (should not pass)
//		Exchange invalidExchange = anExchange(context).build();
//		Order invalidOrder = createNewOrder("X7XXX-XX4X");
//		invalidExchange.getIn().setBody(invalidOrder);
//		assertPredicateDoesNotMatch(validateZipCode(), invalidExchange);
		Assert.fail("Not implemented yet");

	}
	
	@Test
	public void testValidNumberOfItemsPredicates() {
		//Evaluate if the validateNumberOfItems predicate is working using an order with three order items (should pass)
//		Exchange validExchange = anExchange(context).build();
//		Order order = createNewOrder(new OrderItem(), new OrderItem(), new OrderItem());
//		validExchange.getIn().setBody(order);
//		assertPredicateMatches(validateNumberOfItems(), validExchange);
		Assert.fail("Not implemented yet");

	}

	@Test
	public void testInvalidNumberOfItemsPredicate(){
		// Evaluate if the validateNumberOfItems predicate is working using an order with three order items (should not pass)
//		Exchange invalidExchange = anExchange(context).build();
//		Order invalidOrder = createNewOrder(new OrderItem());
//		invalidExchange.getIn().setBody(invalidOrder);
//		assertPredicateDoesNotMatch(validateNumberOfItems(), invalidExchange);
		Assert.fail("Not implemented yet");


	}
	
	private Order createNewOrder(String zipCode) {
		Order order = new Order();
		Customer newCustomer = new Customer();
		Address newAddress = new Address();
		newAddress.setPostalCode(zipCode);
		newCustomer.setShippingAddress(newAddress);
		order.setCustomer(newCustomer);
		return order;
	}

	
	private Order createNewOrder(OrderItem... items) {
		Order order = new Order();
		for (OrderItem orderItem : items) {
			order.getItems().add(orderItem);
		}
		return order;
	}
	
	
	
}
