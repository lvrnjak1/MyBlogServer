package ba.unsa.etf.zavrsni.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@RequiredArgsConstructor
@SpringBootApplication
public class AppApplication {
//	private final AccountService accountService;
//	private final FollowService followService;
//	private final LikeService likeService;
//	private final PostService postService;
//	private final UserService userService;
//	private final NewPostPublisher newPostPublisher;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

//	@Bean
//	public ServletRegistrationBean graphQLServlet() {
//		return new ServletRegistrationBean(SimpleGraphQLHttpServlet.newBuilder(buildSchema(
//				accountService,
//				followService,
//				likeService,
//				postService,
//				userService,
//				newPostPublisher)).build(),"/graphql");
//	}
//
//	private static GraphQLSchema buildSchema(AccountService accountService, FollowService followService,
//											 LikeService likeService, PostService postService, UserService userService,
//											 NewPostPublisher newPostPublisher
//	) {
//		return SchemaParser
//				.newParser()
//				.file("schema.graphqls")
//				.resolvers( new Query(userService, accountService, postService,followService),
//						new Mutation(accountService, postService, likeService, followService),
//						new Subscription(newPostPublisher),
//						new AccountResolver(accountService, postService, followService),
//						new LikeResolver(),
//						new PostResolver(likeService)
//				)
//				.build()
//				.makeExecutableSchema();
//	}

}
