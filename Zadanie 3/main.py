from sklearn.datasets import load_iris
from sklearn import tree
from sklearn.metrics import confusion_matrix
import matplotlib.pyplot as plt
import seaborn as sns

iris = load_iris()  # wczytujemy zbiór danych irysów
X, y = iris.data, iris.target  # cechy (X) i etykiety klas (y) dla każdego przykładu w zbiorze

# Inicjalizujemy klasyfikator drzewa decyzyjnego
clf = tree.DecisionTreeClassifier()
# Trenujemy klasyfikator drzewa decyzyjnego na danych X i y.
# Metoda fit() dopasowuje model do danych treningowych,
# ucząc go, jak przewidywać etykiety klas na podstawie dostarczonych cech
clf = clf.fit(X, y)

# Wyświetlamy diagram drzewa decyzyjnego
tree.plot_tree(clf)
plt.show()

# Analizujemy wyniki trenowania
predicted_labels = clf.predict(X)  # przewidziane etykiety klas na podstawie danych treningowych
accuracy = clf.score(X, y)  # dokładność klasyfikatora na danych treningowych

# Wyświetlamy macierz pomyłek
confusion_mat = confusion_matrix(y, predicted_labels)
print("Macierz pomyłek:")
print(confusion_mat)

# Wyświetlamy macierz pomyłek w postaci graficznej
labels = iris.target_names
sns.heatmap(confusion_mat, annot=True, xticklabels=labels, yticklabels=labels)
plt.xlabel('Przewidziane etykiety')
plt.ylabel('Rzeczywiste etykiety')
plt.show()

print("Przewidziane etykiety klas:", predicted_labels)
print("Dokładność klasyfikatora:", accuracy)


