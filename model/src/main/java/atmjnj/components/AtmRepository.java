package atmjnj.components;


import atmjnj.components.Atm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtmRepository extends JpaRepository<Atm, Long> {
    Atm findById(int Id);
}
