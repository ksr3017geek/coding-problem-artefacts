package com.loanbridge.processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.loanbridge.bean.BalanceDTO;
import com.loanbridge.bean.LoanDTO;
import com.loanbridge.bean.PaymentDTO;


public class LoanProcessor_bkp18052022 {
	static Map<String, List<LoanDTO>> bankMap = new HashMap<>();
	static List<LoanDTO> loanDtlsLst;
	enum DupKeyOption {
	    OVERWRITE, DISCARD
	}
	private static Consumer<LoanDTO> createLoanMap = new Consumer<LoanDTO>() {
	    public void accept(LoanDTO loanRecordVO) {
	    	if(bankMap.containsKey(loanRecordVO.getBankName())) {
	    		bankMap.get(loanRecordVO.getBankName()).add(loanRecordVO);
	    	}else {
	    		loanDtlsLst = new ArrayList<>();
	    		loanDtlsLst.add(loanRecordVO);
	    		bankMap.put(loanRecordVO.getBankName(), loanDtlsLst);
	    	}
	    }
	};
	public static void main(String[] args){
		
		String fileName = "C://codeui//loaninputfile.txt";
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
			loanList.forEach(createLoanMap);
			payList = supplier.get()
					.filter(line -> line.startsWith("PAYMENT"))
					.map(s -> s.split(" "))
				    .map(arr -> new PaymentDTO(arr[1],arr[2], Double.parseDouble(arr[3]), Integer.parseInt(arr[4])))
					.collect(Collectors.toList());
			balanceList = supplier.get()
					.filter(line -> line.startsWith("BALANCE"))
					.map(s -> s.split(" "))
				    .map(arr -> new BalanceDTO(arr[1], arr[2], Integer.parseInt(arr[3])))
					.collect(Collectors.toList());
			
			 Stream.of(bankMap.keySet().toString()).forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
