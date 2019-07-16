package org.needlehack.collector.usecases;

public interface UseCase<T extends UseCaseParams> {

    Object execute(T params);
}
