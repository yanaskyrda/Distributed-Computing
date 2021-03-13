package edu.coursera.concurrent.boruvka.parallel;

import edu.coursera.concurrent.boruvka.BoruvkaFactory;

import edu.coursera.concurrent.ParallelBoruvka.ParallelComponent;
import edu.coursera.concurrent.ParallelBoruvka.ParEdge;

/**
 * A factory for generating components and edges when performing a parallel
 * traversal.
 */
public final class ParBoruvkaFactory implements BoruvkaFactory<ParallelComponent, ParEdge> {
    /**
     * {@inheritDoc}
     */
    @Override
    public ParallelComponent newComponent(final int nodeId) {
        return new ParallelComponent(nodeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParEdge newEdge(final ParallelComponent from, final ParallelComponent to, final double weight) {
        return new ParEdge(from, to, weight);
    }
}
