package web.ozon;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import lib.entity.dto.entity.CommentEntity;
import lib.entity.dto.entity.ProductEntity;
import lib.entity.dto.entity.PurchaseEntity;
import lib.entity.dto.entity.UserEntity;
import lib.entity.dto.repository.CommentRepository;
import lib.entity.dto.repository.ProductRepository;
import lib.entity.dto.repository.PurchaseRepository;
import lib.entity.dto.repository.UserRepository;

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
	@CsvSource({ "the best!,false,false,1,2,true",
			"i hate it,true,false,1,3,true",
			"nice,false,true,3,1,true" })
	void loadTestComments(String content,
			Boolean isAnonymous,
			Boolean isDeleted,
			Long authorId,
			Long productId,
			Boolean isChecked) {
		UserEntity userEntity = userRepository.findById(authorId).get();
		ProductEntity productEntity = productRepository.findById(productId).get();
		CommentEntity commentEntity = new CommentEntity(null, productEntity, userEntity, content, isAnonymous,
				isDeleted, isChecked, false);
		commentRepository.save(commentEntity);
	}
}
