package com.example.jobportal.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzntib1h5",
                "api_key", "844948167225163",
                "api_secret", "mrbXEAtRhACaKIV_MnPxNSWikeE"
        ));
    }
}

//CLOUDINARY_API_KEY=844948167225163
//CLOUDINARY_API_SECRET=mrbXEAtRhACaKIV_MnPxNSWikeE
//        CLOUDINARY_CLOUD_NAME=dzntib1h5
