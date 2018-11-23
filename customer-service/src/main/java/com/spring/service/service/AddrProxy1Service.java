package com.spring.service.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AddrProxy1Service {

  private int counter;
  private AddrSingleton addrSingleton;
  private Addr1SingleTonService addr1SingleTonService;
  private Addr2SingleTonService addr2SingleTonService;

  @Autowired
  public AddrProxy1Service(
      Addr1SingleTonService addr1SingleTonService,
      Addr2SingleTonService addr2SingleTonService
  ) {
    this.addr1SingleTonService = addr1SingleTonService;
    this.addr2SingleTonService = addr2SingleTonService;
    this.counter = 0;
  }

  public void setAddrSingleton(int type) {
    this.counter+=1;
    if(type==1){
      this.addrSingleton = addr1SingleTonService;
    }else{
      this.addrSingleton = addr2SingleTonService;
    }
    System.out.println("debug");
  }

  public List<Integer> getCounter() {
    List<Integer> counterList = new ArrayList<>();
    int singleTonCounter = addrSingleton.getCounter();
    counterList.add(singleTonCounter);
    counterList.add(counter);
    return counterList;
  }

}
