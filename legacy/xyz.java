import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public final class XYZ<T extends Comparable<T>> {

    private final Graph<T> g;
    private final Comparator<Node<T>> nodeComparator;
    private final Set<SortedSet<Node<T>>> cliques;

    public BronKerbosch(final Graph<T> g) {
        this.g = g;
        this.nodeComparator = Comparator.comparing(Node::content);
        this.cliques = new LinkedHashSet<>();
    }

    public Set<SortedSet<Node<T>>> compute() {
        this.cliques.clear();
        final SortedSet<Node<T>> p = new TreeSet<>(this.nodeComparator);
        p.addAll(this.g.nodes());
        bk(new TreeSet<>(this.nodeComparator), p, new TreeSet<>(this.nodeComparator));
        return this.cliques;
    }

    private void bk(final SortedSet<Node<T>> r, final SortedSet<Node<T>> p, final SortedSet<Node<T>> x) {
        if (p.isEmpty() && x.isEmpty()) {
            this.cliques.add(r);
            return;
        }
        final SortedSet<Node<T>> pvx = new TreeSet<>(new NodeNeighbourComparator());
        pvx.addAll(p);
        pvx.addAll(x);
        final Node<T> u = pvx.last();
        final SortedSet<Node<T>> pwnu = new TreeSet<>(this.nodeComparator);
        pwnu.addAll(p);
        pwnu.removeAll(u.neighbours());
        for (final Node<T> v : pwnu) {
            final SortedSet<Node<T>> nr = new TreeSet<>(this.nodeComparator);
            nr.addAll(r);
            nr.add(v);
            final SortedSet<Node<T>> np = new TreeSet<>(this.nodeComparator);
            final SortedSet<Node<T>> nx = new TreeSet<>(this.nodeComparator);
            for (final Node<T> neigh : v.neighbours()) {
                if (p.contains(neigh)) {
                    np.add(neigh);
                }
                if (x.contains(neigh)) {
                    nx.add(neigh);
                }
            }
            bk(nr, np, nx);
            p.remove(v);
            x.add(v);
        }
    }

    public List<List<T>> getCliquesAsTLists() {
        final List<List<T>> result = new ArrayList<>();
        for (final Set<Node<T>> clique : this.cliques) {
            final List<T> curList = new ArrayList<>();
            for (final Node<T> node : clique) {
                curList.add(node.content());
            }
            result.add(curList);
        }
        return result;
    }

    private class NodeNeighbourComparator implements Comparator<Node<T>> {

        @Override
        public int compare(final Node<T> n1, final Node<T> n2) {
            if (n1.neighbours().size() > n2.neighbours().size()) {
                return 1;
            } else if (n1.neighbours().size() < n2.neighbours().size()) {
                return -1;
            } else {
                return BronKerbosch.this.nodeComparator.compare(n1, n2);
            }
        }
    }
}