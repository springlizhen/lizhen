package com.chinags.archives.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author XieWenqing
 * @date 2019/11/4 上午 10:57
 */
@Data
@Entity
@Table(name = "T_COA_DOCUMENT_FILEUP")
public class DocumentFileup {
    @Id
    @GenericGenerator(name = "database-uuid", strategy = "uuid")
    @GeneratedValue(generator = "database-uuid")
    @Column(name = "id", nullable = false, length = 120)
    private String id;
    private String name;		// 原文件名称
    private String fileName;  // 文件保存到服务器后的名称
    private String cdfid;		// 卷内文件id
    private String address;		// 原文件存放位置
    private String pdfName;  //pdf文件名称（使用UUID，同filename）
    private String pdfAddress;		// pdf文件存放位置
}
