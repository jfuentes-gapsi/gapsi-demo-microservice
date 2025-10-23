package mx.gapsi.gapsi_demo_microservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToLabels implements Serializable{
    private static final long serialVersionUID = 4837032285290833799L;
    private List<Label> labels;
}
