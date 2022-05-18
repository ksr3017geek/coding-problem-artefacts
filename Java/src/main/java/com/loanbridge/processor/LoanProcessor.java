package com.loanbridge.processor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.loanbridge.bean.BalanceDTO;
import com.loanbridge.bean.LoanDTO;
import com.loanbridge.bean.PaymentDTO;


public class LoanProcessor {
	
	static Map<String, List<Map<String, LoanDTO>>> masterLoanDataMap = new HashMap<>();
	static List<Map<String, LoanDTO>> masterCustDtlsLst = new ArrayList<>();
	static Map<String, LoanDTO> custMap;
	static List<LoanDTO> loanDtlsLst;
	static List<LoanDTO> tmpCustLst;
	static int custCount = 0;

	private static Consumer<LoanDTO> createLoanMasterMap = new Consumer<LoanDTO>() {
		String tmpCustNameStr;
		String tmpCustBankNameStr;
	    public void accept(LoanDTO loanRecordVO) {
	    	tmpCustNameStr = loanRecordVO.getBorrowerName();
	    	tmpCustBankNameStr = loanRecordVO.getBankName();
	    	custMap = new HashMap<>();
	    	custMap.put(tmpCustNameStr, loanRecordVO);
			if(masterLoanDataMap.containsKey(tmpCustBankNameStr)) {
	    		masterLoanDataMap.get(tmpCustBankNameStr).add(custMap);
    		}else {
    			masterCustDtlsLst = new ArrayList<>();
    			masterCustDtlsLst.add(custMap);
    			masterLoanDataMap.put(tmpCustBankNameStr, masterCustDtlsLst);
    		}
			custCount++;
	    }
	};
	
	private static Consumer<PaymentDTO> processPayment = new Consumer<PaymentDTO>() {
		String tmpCustNameStr;
		String tmpCustBankNameStr;
		double lumpAmt;
		public void accept(PaymentDTO paymentRecordVO) {
			tmpCustNameStr = paymentRecordVO.getBorrowerName();
	    	tmpCustBankNameStr = paymentRecordVO.getBankName();
	    	lumpAmt = paymentRecordVO.getLumpsumAmt();
	    	LoanDTO tmpDTO;
    	
	    	if(masterLoanDataMap.containsKey(tmpCustBankNameStr)) {
	    		List<Map<String, LoanDTO>> tmpList = masterLoanDataMap.get(tmpCustBankNameStr);
	    		for( Map<String,LoanDTO> loanMap : tmpList) {
	    			if(loanMap.containsKey(tmpCustNameStr)) {
	    				tmpDTO = loanMap.get(tmpCustNameStr);
	    				tmpDTO.setLumpsum(lumpAmt);
	    			}
	    		}
	    	}
		}
	};
	
	private static Consumer<BalanceDTO> processBalance = new Consumer<BalanceDTO>() {
		String tmpCustNameStr;
		String tmpCustBankNameStr;
		int emicnt;
		LoanDTO tmpDTO;
		double tmpAmtPaid;
		int tmpEmiRemaining;
		double tmpLumpAmt;
		
	    public void accept(BalanceDTO balanceRecordVO) {
	    	tmpCustNameStr = balanceRecordVO.getBorrowerName();
	    	tmpCustBankNameStr = balanceRecordVO.getBankName();
	    	emicnt = balanceRecordVO.getEmiNum();
	    	
	    	if(masterLoanDataMap.containsKey(tmpCustBankNameStr)) {
	    		List<Map<String, LoanDTO>> tmpList = masterLoanDataMap.get(tmpCustBankNameStr);
	    		for( Map<String,LoanDTO> loanMap : tmpList) {
	    			if(loanMap.containsKey(tmpCustNameStr)) {
	    				tmpDTO = loanMap.get(tmpCustNameStr);
	    				tmpLumpAmt = tmpDTO.getLumpsum();
	    				tmpAmtPaid = Math.round((tmpDTO.getMonthlyEmi()*emicnt)+tmpLumpAmt);
	    				if(tmpLumpAmt > 0)
	    					tmpEmiRemaining = tmpDTO.getEmis()-emicnt-(int)Math.round(tmpLumpAmt/tmpDTO.getMonthlyEmi());
	    				else
	    					tmpEmiRemaining = tmpDTO.getEmis()-emicnt;
	    				System.out.print(tmpDTO.getBankName()+" "+tmpDTO.getBorrowerName()+" "+(int)Math.round(tmpAmtPaid)+" "+tmpEmiRemaining);
	    				System.out.println();
	    			}
	    		}
	    	}
	    }
	};
	
	public static void main(String[] args){
		String fileName=null;
		if(args != null)
			fileName=args[0];
		else
			fileName="loaninputfile.txt";
	    List<LoanDTO> loanList = new ArrayList<>();
	    List<PaymentDTO> payList = new ArrayList<>();
	    List<BalanceDTO> balanceList = new ArrayList<>();
		try {
			List<String> allLines = Files.readAllLines(Paths.get(fileName));
			Supplier<Stream<String>> supplier = () -> allLines.stream();
			
			loanList = supplier.get()
					.filter(line -> line.startsWith("LOAN"))
					.map(s -> s.split(" "))
				    .map(arr -> new LoanDTO(arr[1], arr[2], Double.parseDouble(arr[3]), Integer.parseInt(arr[4]), Float.parseFloat(arr[5])))
					.collect(Collectors.toList());
			loanList.forEach(createLoanMasterMap);
			
			payList = supplier.get()
					.filter(line -> line.startsWith("PAYMENT"))
					.map(s -> s.split(" "))
				    .map(arr -> new PaymentDTO(arr[1], arr[2], Double.parseDouble(arr[3]), Integer.parseInt(arr[4])))
					.collect(Collectors.toList());
			payList.forEach(processPayment);
			
			balanceList = supplier.get()
					.filter(line -> line.startsWith("BALANCE"))
					.map(s -> s.split(" "))
				    .map(arr -> new BalanceDTO(arr[1], arr[2], Integer.parseInt(arr[3])))
					.collect(Collectors.toList());
			balanceList.forEach(processBalance);
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
