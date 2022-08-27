package ru.alekseiadamov.lovikod.storage;


import ru.alekseiadamov.lovikod.PromoCodeInfo;

import java.util.Collection;
import java.util.Map;

public interface LinksStorage {
    boolean storeAll(Collection<PromoCodeInfo> links);

    Map<String, PromoCodeInfo> get();
}
