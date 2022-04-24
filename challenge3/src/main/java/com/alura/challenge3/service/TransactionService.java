package com.alura.challenge3.service;

import com.alura.challenge3.model.Transaction;
import com.alura.challenge3.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    private final String UPLOAD_DIR = "C:\\Users\\juliana.blicharz\\Desktop\\financialDomowe\\";
    private final String COMMA = ",";

    public List<Transaction> saveFileToDiretory(MultipartFile file, String fileName) throws IOException {
        double size = getFileSizeMegabytes(file);
        Path path = Paths.get(UPLOAD_DIR + fileName + "_" + LocalDateTime.now().format((DateTimeFormatter.ofPattern("yyMMddHHmmss-"))));

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        log.info(String.format("Arquivo foi copiado para o diretório com sucesso. fileName: %s; size: %.4f mb", fileName, size));

        return getTransactionsFromFile(new Scanner(path.toFile()));
    }

    public List<Transaction> getTransactionsFromFile(Scanner scan) {
        List<Transaction> transactions = new ArrayList<>();
        while (scan.hasNext()) {
            String[] inputLine = scan.nextLine().split(COMMA);
            final Transaction transaction = mapToTransaction(inputLine);
            validateFields(transaction)
                .ifPresent(transactions::add);
        }

        transactions.forEach(transaction -> {
            System.out.println("Transação: " + transaction.toString());
        });

        return transactions;
    }

    private Transaction mapToTransaction(String[] inputLine) {
        return Transaction.builder()
            .bankOrigin(inputLine[0].trim())
            .branchOrigin(inputLine[1].trim())
            .accountOrigin(inputLine[2].trim())
            .bankDestination(inputLine[3].trim())
            .branchDestination(inputLine[4].trim())
            .accountDestination(inputLine[5].trim())
            .amount(ObjectUtils.isEmpty((inputLine[6].trim())) ? null : new BigDecimal(inputLine[6].trim()))
            .dateTime(LocalDateTime.parse(inputLine[7].trim()))
            .fileProcessedDate(LocalDateTime.now())
            .build();
    }

    private Optional<Transaction> validateFields(Transaction transaction) {
        return Optional.ofNullable(transaction)
            .filter(response -> Objects.nonNull(response.getBankOrigin()) && !ObjectUtils.isEmpty(response.getBankOrigin()))
            .filter(response -> Objects.nonNull(response.getBranchOrigin()) && !ObjectUtils.isEmpty(response.getBranchOrigin()))
            .filter(response -> Objects.nonNull(response.getAccountOrigin()) && !ObjectUtils.isEmpty(response.getAccountOrigin()))
            .filter(response -> Objects.nonNull(response.getBankDestination()) && !ObjectUtils.isEmpty(response.getBankDestination()))
            .filter(response -> Objects.nonNull(response.getBranchDestination()) && !ObjectUtils.isEmpty(response.getBranchDestination()))
            .filter(response -> Objects.nonNull(response.getAccountDestination()) && !ObjectUtils.isEmpty(response.getAccountDestination()))
            .filter(response -> Objects.nonNull(response.getAmount()) && !ObjectUtils.isEmpty(response.getAmount()))
            .filter(response -> Objects.nonNull(response.getDateTime()) && !ObjectUtils.isEmpty(response.getDateTime()))
            .stream().findFirst();
    }

    private double getFileSizeMegabytes(MultipartFile file) {
        double bytes = file.getSize();
        return (bytes / (1024 * 1024));
    }

    public void saveTransactions(List<Transaction> transactions, Transaction firstFileTransaction) {
        final List<Transaction> savedTransactions = validateTransactions(transactions, firstFileTransaction.getDateTime())
            .stream()
            .map(this::save)
            .collect(Collectors.toList());
        log.info("Transações salvas: [{}]", savedTransactions);
    }

    private List<Transaction> validateTransactions(List<Transaction> transactions, final LocalDateTime dateTime) {
        return transactions.stream()
            .filter(transaction -> dateTime.toString().equals(transaction.getDateTime().toString()))
            .collect(Collectors.toList());
    }

    public Boolean canProcessTransactionOfTheDay(Transaction transaction) {
        LocalDate transactionDay = transaction.getDateTime().toLocalDate();
        return ObjectUtils.isEmpty(repository.findFirstByDateTimeBetween(transactionDay.atStartOfDay(), transactionDay.plusDays(1).atStartOfDay()));
    }

    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    private Transaction alreadyExistTransation(Transaction transaction) {
        log.info("Transação já informada");
        return transaction;
    }

    public List<Transaction> getAll() {
        return repository.findAll();
    }
}
