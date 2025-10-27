package com.my.myaicodemother.service.impl;

import com.my.myaicodemother.service.AppService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.my.myaicodemother.mode.entity.App;
import com.my.myaicodemother.mapper.AppMapper;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author 廖祁新
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService {

}
