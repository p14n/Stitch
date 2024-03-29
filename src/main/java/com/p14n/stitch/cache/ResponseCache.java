package com.p14n.stitch.cache;

import com.p14n.stitch.Function1;
import com.p14n.stitch.StitchException;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by Dean Pehrsson-Chapman
 * Date: 12/04/2013
 */

public class ResponseCache<Request, Response> implements
        Function1<Request, Response> {

    private ConcurrentHashMap<Request, Future<Response>> cache = new ConcurrentHashMap<>();
    private Function1<Request, Response> function;

    public ResponseCache(Function1<Request, Response> function) {
        super();
        this.function = function;
    }

    @Override
    public Response apply(final Request request) {
        while (true) {
            Future<Response> f = cache.get(request);
            try {
                if (f == null) {

                    Callable<Response> callable = new Callable<Response>() {
                        @Override
                        public Response call() throws Exception {
                            return function.apply(request);
                        }
                    };
                    FutureTask<Response> ft = new FutureTask<>(callable);
                    ft.run();
                    f = cache.putIfAbsent(request, ft);
                }
                return get(f);
            } catch (CancellationException e) {
                cache.remove(request, f);
            }
        }
    }

    private Response get(Future<Response> f) {
        try {
            return f.get();
        } catch (CancellationException e) {
            throw e;
        } catch (Exception e) {
            throw new StitchException("Exception found in cache", e);
        }
    }

}
