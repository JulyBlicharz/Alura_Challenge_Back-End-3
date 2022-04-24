package com.alura.challenge3.api;

import com.alura.challenge3.model.Transaction;
import com.alura.challenge3.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Controller
public class TransactionApi {

    private final TransactionService transactionService;

    @GetMapping("/")
    public String homepage(Model model) {
        final List<Transaction> transactions = transactionService.getAll();
        model.addAttribute("transactions", transactions);
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes, Model model) {

        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Selecione um arquivo para enviar.");
            return "redirect:/";
        }

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            final List<Transaction> transactions = transactionService.saveFileToDiretory(file, fileName);

            Transaction firstFileTransaction = transactions.get(0);

            if (canProcessTransactionOfTheDay(firstFileTransaction)) {
                transactionService.saveTransactions(transactions, firstFileTransaction);

                attributes.addFlashAttribute("message", "Arquivo enviado com sucesso: " + fileName);
                return "redirect:/";

            } else {
                attributes.addFlashAttribute("message",
                    String.format("Transações da data %s já foram processadas anteriormente e serão ignoradas", firstFileTransaction.getDateTime().toLocalDate()));
                return "redirect:/";
            }

        } catch (IOException e) {
            handlingError(attributes, e);
            return String.format("Error message: %s", e.getMessage());
        }
    }

    private Boolean canProcessTransactionOfTheDay(Transaction firstFileTransaction) {
        return transactionService.canProcessTransactionOfTheDay(firstFileTransaction);
    }

    private void handlingError(RedirectAttributes attributes, Exception e) {
        log.error("Ocorreu um erro", e);
        String error = String.format("Ocorreu um erro: %s", e.getMessage());
        attributes.addFlashAttribute("erro", error);
    }
}
