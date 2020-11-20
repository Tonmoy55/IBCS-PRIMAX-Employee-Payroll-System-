package com.techhub.service;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techhub.dto.SalaryGenerationDTO;
import com.techhub.idao.IBankAccountDao;
import com.techhub.idao.IEmployeeDao;
import com.techhub.iservice.ISalaryGenerationService;
import com.techhub.model.BankAccount;
import com.techhub.model.Employee;
import com.techhub.repository.BankAccountRepository;

@Service
public class SalaryGenerationService implements ISalaryGenerationService {

	@Autowired
	private IEmployeeDao iEmployeeDao;
	@Autowired
	private IBankAccountDao iBankAccountDao;
	@Autowired
	private BankAccountRepository bankAccountRepository;

	private BigInteger empBasicSalary;
	private BigInteger empHouseRent;
	private BigInteger empMedicalAllowance;
	private BigInteger empTotalSalary;
	private BigInteger companyNewBalance;
	private BigInteger empNewBalance;
	private final int companyBankAccountId = 7;
	private final BigInteger basicSalary = new BigInteger("5000");
	private final BigInteger numeratorHouseRent = new BigInteger("20");
	private final BigInteger denominatorHouseRent = new BigInteger("100");
	private final BigInteger numeratorMedicalAllowance = new BigInteger("15");
	private final BigInteger denominatorMedicalAllowance = new BigInteger("100");

	@Override
	public void salaryGeneration(SalaryGenerationDTO salaryGenerationDTO) {

		Employee employee = iEmployeeDao.findById(salaryGenerationDTO.getEmployeeId());

		// Employee Basic Salary Calculation
		if (employee.getGrade().getId() == 1) {
			empBasicSalary = salaryGenerationDTO.getBasicSalaryOfLowestGrade();
		} else if (employee.getGrade().getId() == 2) {
			empBasicSalary = salaryGenerationDTO.getBasicSalaryOfLowestGrade();
		} else if (employee.getGrade().getId() == 21) {
			empBasicSalary = salaryGenerationDTO.getBasicSalaryOfLowestGrade()
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade()).add(basicSalary);
		} else if (employee.getGrade().getId() == 22) {
			empBasicSalary = salaryGenerationDTO.getBasicSalaryOfLowestGrade()
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade())
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade()).add(basicSalary);
		} else if (employee.getGrade().getId() == 23) {
			empBasicSalary = salaryGenerationDTO.getBasicSalaryOfLowestGrade()
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade())
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade())
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade()).add(basicSalary);
		} else if (employee.getGrade().getId() == 24) {
			empBasicSalary = salaryGenerationDTO.getBasicSalaryOfLowestGrade()
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade())
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade())
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade())
					.add(salaryGenerationDTO.getBasicSalaryOfLowestGrade()).add(basicSalary);
		}

		// Employee House Rent Calculation
		empHouseRent = (empBasicSalary.multiply(numeratorHouseRent)).divide(denominatorHouseRent);

		// Employee Medical Allowance Calculation
		empMedicalAllowance = (empBasicSalary.multiply(numeratorMedicalAllowance)).divide(denominatorMedicalAllowance);

		// Employee Total Salary Calculation
		empTotalSalary = empBasicSalary.add(empHouseRent).add(empMedicalAllowance);

		// Companies New Account Balance
				BankAccount companyBankAccount = bankAccountRepository.findById(companyBankAccountId).orElse(null);
				companyNewBalance = companyBankAccount.getBalance().subtract(salaryGenerationDTO.getCompanyAccountBalance());
				
		// update Employee Account Balance
		BankAccount empBankAccount = bankAccountRepository.findByAccountno(employee.getBankAccount().getAccountno());
		empNewBalance = empBankAccount.getBalance().add(empTotalSalary);
		bankAccountRepository.updateEmployeeBankAccountInfo(employee.getBankAccount().getAccountno(), empNewBalance);

		

		// Update Company Account Balance
		bankAccountRepository.updateCompanyBankAccountInfo(companyBankAccountId, companyNewBalance);

	}

}
