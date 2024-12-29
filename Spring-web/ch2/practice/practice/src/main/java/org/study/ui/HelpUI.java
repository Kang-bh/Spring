package org.study.ui;


import org.study.utils.Task;

public class HelpUI {
    public static void displayHelp() {
        System.out.println("도움말 :");
        System.out.println("종료: 99");
        System.out.printf("%s: %d, ", "고객 정보 입력", Task.CUST_INSERT.getId());
        System.out.printf("%s: %d, ", "고객 정보 조회", Task.CUST_GET.getId());
        System.out.printf("%s: %d, ", "고객 전체 조회", Task.CUST_GETALL.getId());
        System.out.printf("%s: %d, ", "고객 정보 변경", Task.CUST_UPDATE.getId());
        System.out.printf("%s: %d, ", "고객 정보 삭제", Task.CUST_DELETE.getId());
        System.out.printf("%s: %d \n", "고객 정보 자동 저장", Task.CUST_AUTO.getId());
        System.out.printf("%s: %d, ", "제품 정보 입력", Task.PROD_INSERT.getId());
        System.out.printf("%s: %d, ", "제품 정보 조회", Task.PROD_GET.getId());
        System.out.printf("%s: %d, ", "제품 전체 조회", Task.PROD_GETALL.getId());
        System.out.printf("%s: %d, ", "제품 정보 변경", Task.PROD_UPDATE.getId());
        System.out.printf("%s: %d, ", "제품 정보 삭제", Task.PROD_DELETE.getId());
        System.out.printf("%s: %d \n", "제품 정보 자동 저장", Task.PROD_AUTO.getId());
        System.out.printf("%s: %d, ", "재고 정보 입력", Task.INVTR_STORE.getId());
        System.out.printf("%s: %d, ", "재고 정보 조회", Task.INVTR_GET.getId());
        System.out.printf("%s: %d, ", "재고 전체 조회", Task.INVTR_GETALL.getId());
        System.out.printf("%s: %d, ", "제품 출고", Task.INVTR_TAKE.getId());
        System.out.printf("%s: %d \n", "재고 정보 자동 저장", Task.INVTR_AUTO.getId());
        System.out.printf("%s: %d, ", "제품 주문", Task.ORDR_PURCHASE.getId());
        System.out.printf("%s: %d, ", "주문 정보 조회", Task.ORDR_GET.getId());
        System.out.printf("%s: %d, ", "고객 주문 조회", Task.ORDR_GETALL.getId());
        System.out.printf("%s: %d \n", "주문 취소", Task.ORDR_CANCEL.getId());
    }
}
