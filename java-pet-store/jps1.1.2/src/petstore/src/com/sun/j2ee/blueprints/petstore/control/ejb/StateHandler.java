package com.sun.j2ee.blueprints.petstore.control.ejb;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;

import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;


public interface StateHandler  {

  public void init(StateMachine urc);

  public void doStart();

  public void perform(EStoreEvent event) throws EStoreEventException;

  public void doEnd();
}
