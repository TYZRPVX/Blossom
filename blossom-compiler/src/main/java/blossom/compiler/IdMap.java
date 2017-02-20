package blossom.compiler;


import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;


public class IdMap {

    private Map<Integer, Class<? extends Annotation>> idMap;

    public IdMap() {
        idMap = new HashMap<>();
    }

    public void put(Integer id, Class<? extends Annotation> annoClass) {
        idMap.put(id, annoClass);
    }

    public boolean containsKey(Integer id) {
        return idMap.containsKey(id);
    }


}
