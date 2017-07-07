package com.redhat.training.jms.transacted;

import org.apache.camel.spring.spi.SpringTransactionPolicy;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Named;

@Named("PROPAGATION_REQUIRED")
public class JmsPropagationRequiredPolicy extends SpringTransactionPolicy {
  @Inject
  public JmsPropagationRequiredPolicy(JmsTransactionManager cdiTransactionManager) {
    super(new TransactionTemplate(cdiTransactionManager,
      new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED)));
  }
}
