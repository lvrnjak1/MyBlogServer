package ba.unsa.etf.zavrsni.app.graphql.utils;

import ba.unsa.etf.zavrsni.app.model.Account;
import ba.unsa.etf.zavrsni.app.model.Post;
import ba.unsa.etf.zavrsni.app.services.FollowService;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.springframework.stereotype.Component;

@Component
public class NewPostPublisher {
    private final Flowable<Post> publisher;
    private ObservableEmitter<Post> emitter;
    private final FollowService followService;

    public NewPostPublisher(FollowService followService) {
        this.followService = followService;
        Observable<Post> newPostObservable = Observable.create(emitter -> {
            this.emitter = emitter;
        });

        ConnectableObservable<Post> connectableObservable = newPostObservable.share().publish();
        connectableObservable.connect();
        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void publish(final Post post) {
        emitter.onNext(post);
    }

    public Flowable<Post> getPublisher(Account account) {
        return publisher.filter(post -> followService
                .findByFollowerAndFollowee(account, post.getAuthor())
                .isPresent());
    }
}
