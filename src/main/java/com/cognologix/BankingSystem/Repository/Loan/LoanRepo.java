package com.cognologix.BankingSystem.Repository.Loan;

import com.cognologix.BankingSystem.Model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepo extends JpaRepository<Loan,Integer> {
    public Boolean existsByLoanAccountNumber(Integer accountNumber);

    public Loan findByLoanAccountNumber(Integer accountNumber);

    public Loan findByLoanNumber(Integer loanNumber);

    public Boolean existsByLoanNumber(Integer loanNumber);
}
