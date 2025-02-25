package web.ozon;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import web.ozon.entity.CommentEntity;
import web.ozon.entity.ProductEntity;
import web.ozon.entity.PurchaseEntity;
import web.ozon.entity.UserEntity;
import web.ozon.repository.CommentRepository;
import web.ozon.repository.ProductRepository;
import web.ozon.repository.PurchaseRepository;
import web.ozon.repository.UserRepository;

@SpringBootTest
class OzonApplicationTests {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PurchaseRepository purchaseRepository;

	@ParameterizedTest
	@CsvSource({ "name,passwors,false,USER",
			"log,pass,false,USER",
			"1,1,false,USER",
			"admin,admin,false,ADMIN" })
	void loadTestUsers(String login, String password, Boolean isDeleted, String role) {
		String newPassword = passwordEncoder.encode(password);
		UserEntity userEntity = new UserEntity(null, login, newPassword, role, isDeleted);
		userRepository.save(userEntity);
	}

	@ParameterizedTest
	@CsvSource({ "lamp,false,1",
			"phone,false,log",
			"sofa,false,name" })
	void loadTestProducts(String content, Boolean isDeleted, String sellerLogin) {
		UserEntity userEntity = userRepository.findByLogin(sellerLogin);
		ProductEntity productEntity = new ProductEntity(null, userEntity, content, isDeleted);
		productRepository.save(productEntity);
	}

	@ParameterizedTest
	@CsvSource({ "1,3,false",
			"1,2,true",
			"2,1,false",
			"3,1,true",
			"3,2,false",
			"3,3,false" })
	void loadTestPurchases(Long ownerId, Long productId, Boolean isCommented) {
		UserEntity userEntity = userRepository.findById(ownerId).get();
		ProductEntity productEntity = productRepository.findById(productId).get();
		PurchaseEntity purchaseEntity = new PurchaseEntity(null, userEntity, productEntity, isCommented);
		purchaseRepository.save(purchaseEntity);
	}

	@ParameterizedTest
	@CsvSource({ "the best!,false,false,1,2",
			"i hate it,true,false,1,3",
			"nice,false,true,3,1" })
	void loadTestComments(String content, Boolean isAnonymous, Boolean isDeleted, Long authorId, Long productId) {
		UserEntity userEntity = userRepository.findById(authorId).get();
		ProductEntity productEntity = productRepository.findById(productId).get();
		CommentEntity commentEntity = new CommentEntity(null, productEntity, userEntity, content, isAnonymous,
				isDeleted);
		commentRepository.save(commentEntity);
	}
}
