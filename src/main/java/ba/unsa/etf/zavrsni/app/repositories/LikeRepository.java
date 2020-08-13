package ba.unsa.etf.zavrsni.app.repositories;

import ba.unsa.etf.zavrsni.app.model.Like;
import ba.unsa.etf.zavrsni.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByAccount_IdAndLikedPost_Id(Long accountId, Long postId);
    List<Like> findAllByLikedPost(Post post);
    int countByLikedPost(Post post);
}
