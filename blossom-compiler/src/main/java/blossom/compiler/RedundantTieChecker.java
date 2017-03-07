package blossom.compiler;


import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;

/**
 * ensure the same annotation can't be used on the same ID
 */
public class RedundantTieChecker {

    private final Set<Bead> tieChain;

    public RedundantTieChecker() {
        tieChain = new HashSet<>();
    }

    public void check(Integer id, Class<? extends Annotation> annoClass, Element element) {

        Bead bead = new Bead(id, annoClass);
        if (!tieChain.contains(bead)) {
            tieChain.add(bead);
            return;
        }
        ProcessMessager.error(element, "Attempt to use @%s for an already ID %d"
                , annoClass.getSimpleName(), id);

    }

    private static class Bead {

        final int id;
        final Class<? extends Annotation> annoClass;

        Bead(int id, Class<? extends Annotation> annoClass) {
            this.id = id;
            this.annoClass = annoClass;
        }

        /**
         * ensure the same id with the same tie annotation throw an exception
         * eg. OnClick1 == OnClick1, OnClick1 != OnLongClick1
         */
        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!Bead.class.isAssignableFrom(o.getClass())) {
                return false;
            }
            final Bead other = (Bead) o;
            if (this.annoClass != other.annoClass || this.id != other.id) {
                return false;
            }
            return true;
        }


        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + id;
            hash = 31 * hash + (annoClass == null ? 0 : annoClass.hashCode());
            return hash;
        }
    }
}
