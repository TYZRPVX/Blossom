package blossom.compiler;


import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;


public class RepetitiveTieChecker {

    private Set<Atom> tieChain;

    public RepetitiveTieChecker() {
        tieChain = new HashSet<>();
    }

    public void check(Integer id, Class<? extends Annotation> annoClass, Element element) {

        Atom atom = new Atom(id, annoClass);
        if (!tieChain.contains(atom)) {
            tieChain.add(atom);
            return;
        }
        ProcessMessager.error(element, "Attempt to use @%s for an already ID %d"
                , annoClass.getSimpleName(), id);

    }

    static class Atom {

        int id;
        Class<? extends Annotation> annoClass;

        public Atom(int id, Class<? extends Annotation> annoClass) {
            this.id = id;
            this.annoClass = annoClass;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (!Atom.class.isAssignableFrom(o.getClass())) {
                return false;
            }
            final Atom other = (Atom) o;
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
