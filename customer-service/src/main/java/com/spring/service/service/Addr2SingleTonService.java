package com.spring.service.service;

import org.springframework.stereotype.Service;

@Service
public class Addr2SingleTonService implements AddrSingleton {

  private int counter;

  public Addr2SingleTonService() {
    counter = 50;
  }

  @Override
  public int getCounter() {
    counter += 1;
    return counter;
  }
}
