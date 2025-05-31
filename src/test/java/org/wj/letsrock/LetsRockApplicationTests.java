package org.wj.letsrock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wj.letsrock.application.image.ImageService;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;

@SpringBootTest
@Slf4j
class LetsRockApplicationTests {
	@Autowired
	private ImageService imageService;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private PasswordEncoder  passwordEncoder;
	@Autowired
	private ArticleReadService articleReadService;

	@Test
	void testisExistImage() {
		String fileName="http://127.0.0.1:8080/image/0ff24caa-2907-4740-b4e8-93cddea6c1fd.jpg";
		log.info("{}",imageService.isImageExist(fileName));
	}

	@Test
	void testTransferImg(){
    }

	@Test
	void testMdImage(){
		String md="testtest52812ds![图片](https://bookstore-book-image.oss-cn-nanjing.aliyuncs.com/8_cover_f0670683-11a0-4d6e-90ef-f87ce0752db9_b_5d66273b70541908f3f5083d05848c95.jpg)testtest\n" +
				"\n" +
				"![图片](https://bookstore-book-image.oss-cn-nanjing.aliyuncs.com/7_cover_8c4fd0e9-9161-47e0-a283-a587e9eceb50_b_5d66273b70541908f3f5083d05848c95.jpg)\n";

		try{
			System.out.println(imageService.mdImgReplace(md));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	void testGeneratePassword(){
		log.info(passwordEncoder.encode("guest"));
	}

	@Test
	 void testGetLatestArticles(){
		PageResultVo<ArticleDTO> articleDTOListVo = articleReadService.queryLatestArticles(PageParam.newPageInstance(1L, 10L));
		for(ArticleDTO articleDTO : articleDTOListVo.getList()){
			log.info("{}",articleDTO);
		}

	}



}
