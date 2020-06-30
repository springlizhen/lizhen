package com.chinags.file.util;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.pdf.PdfReader;
import net.qiyuesuo.sdk.SDKClient;
import net.qiyuesuo.sdk.api.ContractService;
import net.qiyuesuo.sdk.api.DocumentService;
import net.qiyuesuo.sdk.api.EmployeeService;
import net.qiyuesuo.sdk.api.SignService;
import net.qiyuesuo.sdk.bean.company.TenantType;
import net.qiyuesuo.sdk.bean.contract.*;
import net.qiyuesuo.sdk.bean.document.CreateDocumentRequest;
import net.qiyuesuo.sdk.bean.document.CreateDocumentResult;
import net.qiyuesuo.sdk.bean.document.DocumentDelete;
import net.qiyuesuo.sdk.bean.employee.UserSearchRequest;
import net.qiyuesuo.sdk.bean.sign.SignUrlRequest;
import net.qiyuesuo.sdk.bean.sign.Signatory;
import net.qiyuesuo.sdk.bean.user.UserDetail;
import net.qiyuesuo.sdk.common.exception.PrivateAppException;
import net.qiyuesuo.sdk.common.http.StreamFile;
import net.qiyuesuo.sdk.common.json.JSONUtils;
import net.qiyuesuo.sdk.common.utils.TimeUtils;
import net.qiyuesuo.sdk.impl.ContractServiceImpl;
import net.qiyuesuo.sdk.impl.DocumentServiceImpl;
import net.qiyuesuo.sdk.impl.EmployeeServiceImpl;
import net.qiyuesuo.sdk.impl.SignServiceImpl;
import sun.applet.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author : Mark_Wang
 * @Date : 2020/2/10
 **/
public class QzUtil {

    // 连接设置
    private static final String URL = "http://10.0.2.6:9182";
    // .2服务器
    /*private static final String ACCESS_KEY = "frr0X0YiKW";
    private static final String ACCESS_SECCRET = "2sH1hTfAu3ruX6N6jSwMhd6G2gaugX";*/
    // .6服务器
    private static final String ACCESS_KEY = "pIIlQAjCHP";
    private static final String ACCESS_SECCRET = "QhDxmjFF26NLMhoE7n8hqXe4VGBgoQ";
    // 发起方名字
    private static final String CREATOR_NAME = "郑英";
    // 发起方手机号
    private static final String CREATOT_PHONE = "13791066727";
    // 签署方名字
    /*private static final String TENANT_NAME = "维森集团有限公司";*/
    private static final String TENANT_NAME = "山东省调水工程运行维护中心";
    // 签署方手机号
    private static final String CONTACT = "13791066727";

    public static void main(String[] args) throws Exception {
        Long documentId = createFile("C:\\hz\\HZ808285719c372c0171a5da9fd50f5b.pdf", "测试");
        System.out.println("document: " + documentId);
        List<Long> documents = new ArrayList<>();
        documents.add(documentId);
        String number = "mishuke";
        Long contractId = createContract(documents, "测试", number);
        System.out.println("contractId: " + contractId);
        String url = signUrl(contractId, number);
        System.out.println(url);

        /*downloadDoc(2666836810956718294L ,"12345672");
        System.out.println(presignUrl(2666836811824939227L));*/
        //System.out.println(JSON.toJSONString(detail(2686517164644606256L)));
    }

    /**
     * 用文件创建合同文档接口
     * @param fileName 文件名称
     * @return
     * @throws Exception
     */
    public static Long createFile(String fileName, String title) throws Exception {
        // 初始化
        SDKClient clinet = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        ContractService contractService = new ContractServiceImpl(clinet);
        // 方法调用
        CreateDocumentRequest request = new CreateDocumentRequest();
        File file = new File(fileName);
        // 获取上传文件的类型
        String[] files = fileName.split("\\.");
        String fileType = files[files.length - 1];
        request.setFile(new StreamFile(new FileInputStream(file)));
        request.setFileType(fileType);
        request.setTitle(title);
        List<WaterMarkContent> list = new ArrayList<>();
        WaterMarkContent waterMarkContent1 = new WaterMarkContent();
        waterMarkContent1.setLocation(WaterMarkLocation.UPPER_LEFT);
        waterMarkContent1.setImageBase64("/9j/4AAQSkZJR");
        list.addAll(Arrays.asList(waterMarkContent1));
        request.setWaterMarkConfig(list);
        CreateDocumentResult result = contractService.createByFile(request);
        Long documentId = result.getDocumentId();
        return documentId;
    }

    /**
     * 合同创建
     * @param documents
     * @param hzId
     * @return
     * @throws Exception
     */
    public static Long createContract(List<Long> documents, String hzId, String number) throws Exception {
        UserDetail userDetail = getUserInfo(number);
        // 初始化
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        ContractService contractService = new ContractServiceImpl(client);
        // ⽅法调⽤
        CreateContractRequest createContractRequest = new CreateContractRequest();
        createContractRequest.setDocuments(documents);
        //createContractRequest.setCategoryId(2472609650768990241L);
        createContractRequest.setEndTime(TimeUtils.format(TimeUtils.after(new Date(), 1), TimeUtils.STANDARD_PATTERN));
        createContractRequest.setSend(true);
        createContractRequest.setMustSign(true);
        createContractRequest.setExtraSign(false);
        createContractRequest.setSubject(hzId);
        createContractRequest.setCreatorName(userDetail.getName());
        createContractRequest.setCreatorContact(userDetail.getMobile());
        // createContractRequest.setSend(false);
        List<Signatory> signatories = new ArrayList<Signatory>();
        // 发起⽅
        Signatory signatory1 = new Signatory();
        signatory1.setContact(userDetail.getMobile());
        signatory1.setTenantType(TenantType.CORPORATE);
        signatory1.setTenantName(TENANT_NAME);
        // 添加签署⽅
        signatories.add(signatory1);
        createContractRequest.setSignatories(signatories);
        Long contractId = contractService.createContractByCategory(createContractRequest);
        return contractId;
    }

    /**
     * 根据用户编号获取用户信息
     * @param number
     * @return
     * @throws PrivateAppException
     */
    public static UserDetail getUserInfo(String number) throws PrivateAppException {
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        EmployeeService employeeService = new EmployeeServiceImpl(client);
        //⽅法调⽤
        UserSearchRequest request = new UserSearchRequest();
        request.setNumber(number);
        UserDetail result = employeeService.userDetail(request);
        return result;
    }

    /**
     * 签署页面
     * @param contractId 合同id
     * @return
     * @throws Exception
     */
    public static String signUrl(long contractId, String number) throws Exception {
        UserDetail userDetail = getUserInfo(number);
        // 初始化
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        SignService signService = new SignServiceImpl(client);
        // ⽅法调⽤
        SignUrlRequest request = new SignUrlRequest();
        request.setTenantType(TenantType.CORPORATE);
        request.setTenantName(TENANT_NAME);
        request.setContact(userDetail.getMobile());
        request.setContractId(contractId);
        //回调⻚⾯
        //request.setCallbackPage("");
        String signurl = signService.signUrl(request);
        return signurl;
    }

    /**
     * 下载合同文档
     * @param documentId 文档id
     * @param fileName 本地名称
     * @return result 是否成功
     */
    public static String downloadDoc(Long documentId ,String fileName, String path) {
        String result = null;
        try {
            // 初始化
            SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
            ContractService contractService = new ContractServiceImpl(client);
            // ⽅法调⽤
            OutputStream outputStream = new FileOutputStream(new File(path + fileName + ".pdf"));
            contractService.downloadDoc(documentId, outputStream);
            result = path + fileName + ".pdf";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 合同预签属页面
     * @param contractId
     * @return
     * @throws Exception
     */
    public static String presignUrl(Long contractId) throws Exception {
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        ContractService contractService = new ContractServiceImpl(client);
        //⽅法调⽤
        String presignUrl = contractService.preSignUrl(contractId, CONTACT);
        return presignUrl;
    }

    /**
     * 合同详情
     * @param contractId
     * @return
     * @throws Exception
     */
    public static ContractDetail detail(Long contractId) throws Exception {
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        ContractService contractService = new ContractServiceImpl(client);
        //⽅法调⽤
        ContractDetail contractDetail = contractService.detail(contractId);
        return contractDetail;
    }

    /**
     * 删除合同
     * @param contractId
     * @return
     */
    public static boolean deleteContract(Long contractId) {
        boolean result = true;
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        ContractService contractService = new ContractServiceImpl(client);
        //⽅法调⽤
        try {
            contractService.delete(contractId);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 合同文档解绑
     * @param documentId
     * @param contractId
     * @return
     */
    public static boolean documentDelete(Long documentId, Long contractId) {
        boolean result = true;
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        DocumentService documentService = new DocumentServiceImpl(client);
        //⽅法调⽤

        DocumentDelete docDelete = new DocumentDelete();
        docDelete.setContractId(contractId);
        docDelete.setDocumentIds(Arrays.asList(documentId));
        try {
            documentService.unBind(docDelete);
        } catch (PrivateAppException e) {
            result = false;
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    /**
     * 发起合同
     * @param contractId
     * @throws Exception
     */
    public static void contractSend(Long contractId) throws Exception {
        SDKClient client = new SDKClient(URL, ACCESS_KEY, ACCESS_SECCRET);
        ContractService contractService = new ContractServiceImpl(client);
        //⽅法调⽤
        SendContractRequest request = new SendContractRequest();
        request.setContractId(contractId);
        contractService.send(request);
    }
}
