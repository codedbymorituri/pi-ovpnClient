package certificates;

import java.util.Comparator;

public class CertificateSorter implements Comparator<CertificateData> {

    @Override
    public int compare(CertificateData certificate1, CertificateData certificate2) {
        return certificate1.getName().compareTo(certificate2.getName());
    }

}

