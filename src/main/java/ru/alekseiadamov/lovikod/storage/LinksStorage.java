package ru.alekseiadamov.lovikod;


import java.util.Collection;
import java.util.Map;

public interface LinksStorage {
    boolean storeAll(Collection<PromoCodeInfo> links);

    Map<String, PromoCodeInfo> get();
}
