/*
 * $Id: StateHandlerSupport.java,v 1.5.4.3 2001/03/15 00:39:59 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */


package com.sun.j2ee.blueprints.petstore.control.ejb;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;

import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;

public class StateHandlerSupport implements java.io.Serializable, StateHandler {

  protected StateMachine machine = null;

  public void init(StateMachine machine) {
      this.machine = machine;
  }

  public void doStart() {}

  public void perform(EStoreEvent event) throws EStoreEventException {};

  public void doEnd() {}
}
