package l1j.server.server.utils.random;

public interface RandomGenerator {
  int nextInt();
  
  int nextInt(int paramInt);
  
  int range(int paramInt1, int paramInt2);
  
  int choice(int[] paramArrayOfint);
}


